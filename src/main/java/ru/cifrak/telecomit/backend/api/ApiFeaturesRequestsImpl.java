package ru.cifrak.telecomit.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.cifrak.telecomit.backend.auth.repository.RepositoryAccount;
import ru.cifrak.telecomit.backend.entities.Location;
import ru.cifrak.telecomit.backend.entities.Signal;
import ru.cifrak.telecomit.backend.entities.TcType;
import ru.cifrak.telecomit.backend.entities.User;
import ru.cifrak.telecomit.backend.entities.locationsummary.*;
import ru.cifrak.telecomit.backend.exceptions.NotFoundException;
import ru.cifrak.telecomit.backend.features.comparing.LocationFeature;
import ru.cifrak.telecomit.backend.repository.*;
import ru.cifrak.telecomit.backend.repository.specs.FeatureEditFullSpec;
import ru.cifrak.telecomit.backend.service.LocationService;
import ru.cifrak.telecomit.backend.service.ServiceWritableTc;
import ru.cifrak.telecomit.backend.utils.SortingOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ApiFeaturesRequestsImpl implements ApiFeaturesRequests {

    private static final Map<String, String[]> SORTING_FIELDS = new HashMap<String, String[]>() {{
        put("id", new String[]{"id"});
        put("created", new String[]{"locationFeaturesEditingRequest.created"});
        put("source", new String[]{"locationFeaturesEditingRequest.changeSource"});
        put("action", new String[]{"action"});
        put("user", new String[]{"locationFeaturesEditingRequest.user.oid"});
        put("userName", new String[]{
                "locationFeaturesEditingRequest.user.lastName",
                "locationFeaturesEditingRequest.user.firstName",
                "locationFeaturesEditingRequest.user.patronymicName"});
        put("location", new String[]{"locationFeaturesEditingRequest.location.name"});
        put("locationParent", new String[]{"locationFeaturesEditingRequest.location.locationParent.name"});
    }};

    private static final int PAGE_SIZE_TO_GENERATE_EXCEL = 100;

    private final RepositoryFeaturesRequests repositoryFeaturesRequests;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryLocation repositoryLocation;
    private final RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests;
    private final ServiceWritableTc serviceWritableTc;
    private final LocationService locationService;
    private final RepositoryFeatureEditFulls repositoryFeatureEditFulls;

    public ApiFeaturesRequestsImpl(RepositoryFeaturesRequests repositoryFeaturesRequests,
                                   RepositoryAccount repositoryAccount,
                                   RepositoryLocation repositoryLocation,
                                   RepositoryLocationFeaturesRequests repositoryLocationFeaturesRequests,
                                   ServiceWritableTc serviceWritableTc,
                                   LocationService locationService,
                                   RepositoryFeatureEditFulls repositoryFeatureEditFulls) {
        this.repositoryFeaturesRequests = repositoryFeaturesRequests;
        this.repositoryAccount = repositoryAccount;
        this.repositoryLocation = repositoryLocation;
        this.repositoryLocationFeaturesRequests = repositoryLocationFeaturesRequests;
        this.serviceWritableTc = serviceWritableTc;
        this.locationService = locationService;
        this.repositoryFeatureEditFulls = repositoryFeatureEditFulls;
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requests(Pageable pageable) {
        log.info("->GET /api/features-requests/");
        log.info("<- GET /api/features-requests/");
        return repositoryFeaturesRequests.findAllRequests(pageable);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsAndImportsAndEditions(
            Pageable pageable,
            List<String> sort,
            List<LocationForTable> parents,
            LocalDate contractStart,
            LocalDate contractEnd,
            List<FeatureEditAction> actions,
            List<User> users,
            List<LocationForTable> locations) {
        log.info("->GET /api/features-requests/full/");
        log.info("<-GET /api/features-requests/full/");
        return getLocationFeaturesEditingRequestFulls(
                getFeatureEditFullSpecification(parents, contractStart, contractEnd, actions, users, locations),
                createPageable(pageable, sort));
    }

    @Nullable
    private Specification<FeatureEditFull> getFeatureEditFullSpecification(
            List<LocationForTable> parents,
            LocalDate contractStart,
            LocalDate contractEnd,
            List<FeatureEditAction> actions,
            List<User> users,
            List<LocationForTable> locations) {
        Specification<FeatureEditFull> spec = Objects.requireNonNull(Specification.where(null));
        if (users != null && users.size() > 0) {
            spec = spec.and(FeatureEditFullSpec.inUser(users));
        }
        if (locations != null && locations.size() > 0) {
            spec = spec != null ? spec.and(FeatureEditFullSpec.inLocation(locations)) : null;
        }
        if (parents != null && parents.size() > 0) {
            spec = spec != null ? spec.and(FeatureEditFullSpec.inParent(convertLocationParent2LocationForTable(parents))) : null;
        }
        if (contractStart != null) {
            spec = spec != null ? spec.and(FeatureEditFullSpec.greaterThan(contractStart)) : null;
        }
        if (contractEnd != null) {
            spec = spec != null ? spec.and(FeatureEditFullSpec.lessThan(contractEnd)) : null;
        }
        if (actions != null && actions.size() != 0) {
            spec = spec != null ? spec.and(FeatureEditFullSpec.inAction(actions)) : null;
        }
//        spec = spec != null ? spec.and(FeatureEditFullSpec.excludeDuplicateChanges()) : null;
        return spec;
    }

    @NotNull
    private List<LocationParent> convertLocationParent2LocationForTable(List<LocationForTable> parents) {
        List<LocationParent> locationParents = new ArrayList<>();
        for (LocationForTable parent : parents) {
            locationParents.add(new LocationParent(
                    parent.getId(),
                    parent.getType(),
                    parent.getName()
            ));
        }
        return locationParents;
    }

    @Override
    public ResponseEntity<ByteArrayResource> requestsAndImportsAndEditionsExcel(
            List<String> sort,
            List<LocationForTable> parents,
            LocalDate contractStart,
            LocalDate contractEnd,
            List<FeatureEditAction> actions,
            List<User> users,
            List<LocationForTable> locations) {
        log.info("->GET /api/features-requests/full-excel/");
        Specification<FeatureEditFull> spec = Specification.where(null);
        long timeStart = System.currentTimeMillis();
        List<LocationFeaturesEditingRequestFull> requests = getLocationFeaturesEditingRequestFullsCollection(
                getFeatureEditFullSpecification(parents, contractStart, contractEnd, actions, users, locations),
                sort);
        Workbook book = createBook(requests);
        ByteArrayResource body = createBody(book);
        log.info("<- GET /api/features-requests/full-excel/ (" + (System.currentTimeMillis() - timeStart) / 1000 + " s)");
        return ResponseEntity.ok(body);
    }

    @NotNull
    private ByteArrayResource createBody(Workbook book) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            book.write(baos);
            book.close();
//            try(OutputStream outputStream = new FileOutputStream("journal " + LocalDateTime.now().toString().replaceAll(":", "-") + ".xlsx")) {
//                baos.writeTo(outputStream);
//            }
        } catch (IOException e) {
            log.error("/api/features-requests/full-excel/  error to create byte stream.");
        }
        return new ByteArrayResource(baos.toByteArray());
    }

    @NotNull
    private Workbook createBook(List<LocationFeaturesEditingRequestFull> requests) {
        Workbook book = createBook();
        Sheet sheet = book.getSheetAt(0);
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.GENERAL);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        CreationHelper creationHelper = book.getCreationHelper();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setVerticalAlignment(VerticalAlignment.TOP);
        dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd.mm.yyyy hh:mm:ss"));
        for (int i = 0; i < requests.size(); i++) {
            addRowToBook(sheet, i + 1, style, dateStyle, requests.get(i));
        }
        return book;
    }

    @NotNull
    private List<LocationFeaturesEditingRequestFull> getLocationFeaturesEditingRequestFullsCollection(Specification<FeatureEditFull> spec, List<String> sort) {
        boolean requestsExists = true;
        int pageNumber = 0;
        List<LocationFeaturesEditingRequestFull> requests = new ArrayList<>();
        List<LocationFeaturesEditingRequestFull> requestsToAdd;
        while (requestsExists) {
            requestsToAdd = getLocationFeaturesEditingRequestFulls(spec, createPageable(pageNumber++, sort)).getContent();
            if (requestsToAdd.size() > 0) {
                requests.addAll(requestsToAdd);
            } else {
                requestsExists = false;
            }
        }
        return requests;
    }

    private void addRowToBook(Sheet sheet, int nRow, CellStyle style, CellStyle dateStyle, LocationFeaturesEditingRequestFull request) {
        Row row = sheet.createRow(nRow);
        LocationForTable location = request.getLocation();
        LocationParent locationParent = location.getLocationParent();
        User user = request.getUser();
        FeatureEditFull fef = request.getFeatureEdits().iterator().next();
        row.setRowStyle(style);
        List<Object> rowValues = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(request.getCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date date = new Date(request.getCreated().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        rowValues.add(request.getId());
        rowValues.add(date);
        rowValues.add(request.getChangeSource().toString());
        rowValues.add(fef.getAction().toString());
        rowValues.add(user.getUsername());
        rowValues.add(String.format("%s %s %s", user.getLastName(), user.getFirstName(), user.getPatronymicName()));
        rowValues.add(String.format("%s %s", location.getType(), location.getName()));
        rowValues.add(String.format("%s %s", locationParent.getType(), locationParent.getName()));
        rowValues.add(getChanges(fef));
        Cell cell = row.createCell(0);
        cell.setCellValue((Integer) rowValues.get(0));
        cell = row.createCell(1);
        cell.setCellStyle(dateStyle);
        cell.setCellValue(date);
        cell = row.createCell(2);
        cell.setCellValue((String) rowValues.get(2));
        cell = row.createCell(3);
        cell.setCellValue((String) rowValues.get(3));
        cell = row.createCell(4);
        cell.setCellValue((String) rowValues.get(4));
        cell = row.createCell(5);
        cell.setCellValue((String) rowValues.get(5));
        cell = row.createCell(6);
        cell.setCellValue((String) rowValues.get(6));
        cell = row.createCell(7);
        cell.setCellValue((String) rowValues.get(7));
        cell = row.createCell(8);
        cell.setCellValue((String) rowValues.get(8));
    }

    /**
     * Calculates cell height by information about header string and column width from ExcelColumn object.
     * Calculating is based by default Excel font (Calibri) and default Excel font size (11).
     *
     * @param sheet sheet of book
     * @param columnNumber number of column with header, for which the height is calculated
     * @param rowValues values in cells in row
     * @return cell height, that auto-fit header string with word wrap
     */
    private int getCellHeightByColumnHeaderInfo(Sheet sheet, int columnNumber, List<Object> rowValues) {
        int result = 1;
        int columnWidth = (int) (sheet.getColumnWidth(columnNumber) / 256 + 0.71F);
        Integer[] words = Arrays.stream(rowValues.get(columnNumber).toString().split(" "))
                .map(String::length).toArray(Integer[]::new);
        int limitLength = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i] > columnWidth) {
                result += words[i] / columnWidth - (limitLength == 0 ? 1 : 0);
                int wordRestToAdd = words[i] % columnWidth;
                if (wordRestToAdd != 0) {
                    result++;
                    if (i != words.length - 1) {
                        limitLength = wordRestToAdd;
                    }
                }
            } else if (i != words.length - 1) {
                limitLength += (limitLength == 0 ? 0 : 1) + words[i];
                if (limitLength > columnWidth) {
                    result++;
                    limitLength = 0;
                }
            }
        }
        return result;
    }

    @NotNull
    private String getChanges(FeatureEditFull fef) {
        StringBuilder builder = new StringBuilder();
        LocationFeature tc = fef.getTc();
        TcType tcType = tc.getType();
        if (fef.getAction() == FeatureEditAction.UPDATE) {
            LocationFeature newValue = fef.getNewValue();
            builder.append(String.format("%s %s: ",
                    tcType.toString(),
                    tc.getOperator().getName()));
            if (tcType == TcType.PAYPHONE || tcType == TcType.INFOMAT) {
                builder.append(String.format("Количество %s -> %s",
                        tc.getPayphones(),
                        newValue.getPayphones()));
            } else if (tcType == TcType.INET) {
                builder.append(String.format("Изменен тип подключения с %s (%s) на %s (%s)",
                        tc.getTrunkChannel().getName(),
                        tc.getQuality().toString(),
                        newValue.getTrunkChannel().getName(),
                        newValue.getQuality().toString()));
            } else if (tcType == TcType.MOBILE) {
                builder.append(String.format("Изменен тип сотовой связи с %s (%s) на %s (%s)",
                        tc.getTypeMobile().getName(),
                        tc.getQuality().toString(),
                        newValue.getTypeMobile().getName(),
                        newValue.getQuality().toString()));
            } else if (tcType == TcType.POST) {
                builder.append(String.format("Изменен тип почты с %s на %s",
                        tc.getTypePost().getName(),
                        newValue.getTypePost().getName()));
            } else if (tcType == TcType.RADIO || tcType == TcType.TV) {
                builder.append(String.format("Изменен тип с %s на %s",
                        tc.getTvOrRadioTypes().stream().map(Signal::getName).collect(Collectors.joining(", ")),
                        newValue.getTvOrRadioTypes().stream().map(Signal::getName).collect(Collectors.joining(", "))));
            }
        } else {
            builder.append(String.format("%s: %s %s ",
                    tcType.toString(),
                    fef.getAction().toString(),
                    tc.getOperator().getName()));
            if (tcType == TcType.INET) {
                builder.append(tc.getTrunkChannel().getName());
            } else if (tcType == TcType.MOBILE) {
                builder.append(String.format("%s (%s)",
                        tc.getTypeMobile().getName(),
                        tc.getQuality().toString()));
            } else if (tcType == TcType.POST) {
                builder.append(tc.getTypePost().getName());
            } else if (tcType == TcType.RADIO || tcType == TcType.TV) {
                builder.append(tc.getTvOrRadioTypes().stream().map(Signal::getName).collect(Collectors.joining(", ")));
            }
        }
        return builder.toString();
    }

    private Workbook createBook() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Журнал");
        sheet.setColumnWidth(0, 1823);
        sheet.setColumnWidth(1, 4486);
        sheet.setColumnWidth(2, 3626);
        sheet.setColumnWidth(3, 2507);
        sheet.setColumnWidth(4, 3315);
        sheet.setColumnWidth(5, 6195);
        sheet.setColumnWidth(6, 3563);
        sheet.setColumnWidth(7, 3978);
        sheet.setColumnWidth(8, 8910);
        Row row = sheet.createRow(0);
        row.setHeight((short) (sheet.getDefaultRowHeight() * 2));
        CellStyle style = book.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("№");
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Дата/время изменения");
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("Источник");
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("Действие");
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Пользователь");
        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("ФИО");
        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Населённый пункт");
        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Район");
        cell = row.createCell(8);
        cell.setCellStyle(style);
        cell.setCellValue("Внесенные изменения");
        return book;
    }

    private Sort createSort(List<String> sort) {
        Sort sortingSection = Sort.unsorted();
        if (sort != null && sort.size() == 2
                && SORTING_FIELDS.keySet().stream().anyMatch(field -> field.equals(sort.get(0)))
                && Arrays.stream(SortingOrder.values()).map(Enum::toString).anyMatch(order -> order.equals(sort.get(1).toUpperCase()))) {
            sortingSection = Sort.by(SORTING_FIELDS.get(sort.get(0)));
            if (sort.get(1).toUpperCase().equals(SortingOrder.ASC.toString())) {
                sortingSection = sortingSection.ascending();
            } else {
                sortingSection = sortingSection.descending();
            }
        }
        return sortingSection;
    }

    private Pageable createPageable(Pageable pageable, List<String> sort) {
        if (sort != null && sort.size() == 2
                && SORTING_FIELDS.keySet().stream().anyMatch(field -> field.equals(sort.get(0)))
                && Arrays.stream(SortingOrder.values()).map(Enum::toString).anyMatch(order -> order.equals(sort.get(1).toUpperCase()))) {
            Sort sortingSection = Sort.by(SORTING_FIELDS.get(sort.get(0)));
            if (sort.get(1).toUpperCase().equals(SortingOrder.ASC.toString())) {
                sortingSection = sortingSection.ascending();
            } else {
                sortingSection = sortingSection.descending();
            }
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    sortingSection);
        }
        return pageable;
    }

    private Pageable createPageable(int pageNumber, List<String> sort) {
        return PageRequest.of(pageNumber,
                PAGE_SIZE_TO_GENERATE_EXCEL,
                createSort(sort));
    }

    @NotNull
    private Page<LocationFeaturesEditingRequestFull> getLocationFeaturesEditingRequestFulls(Specification<FeatureEditFull> spec, Pageable pageable) {
        Page<FeatureEditFull> allRequestsAndImportAndEditions = repositoryFeatureEditFulls.findAll(spec, pageable);
        return new PageImpl<>(
                transferFeatureEditFullContent2LocationFeaturesEditingRequestFullContent(allRequestsAndImportAndEditions.getContent()),
                allRequestsAndImportAndEditions.getPageable(),
                allRequestsAndImportAndEditions.getTotalElements());
    }

    private List<LocationFeaturesEditingRequestFull> transferFeatureEditFullContent2LocationFeaturesEditingRequestFullContent(List<FeatureEditFull> content) {
        List<LocationFeaturesEditingRequestFull> result = new ArrayList<>();
        for (FeatureEditFull featureEdit : content) {
            LocationFeaturesEditingRequestFull request2LocationFeaturesEditingRequestFull = new LocationFeaturesEditingRequestFull();
            LocationFeaturesEditingRequestFull1 requestFromFeatureEditFull = featureEdit.getLocationFeaturesEditingRequest();
            request2LocationFeaturesEditingRequestFull.setId(requestFromFeatureEditFull.getId());
            request2LocationFeaturesEditingRequestFull.setLocation(requestFromFeatureEditFull.getLocation());
            request2LocationFeaturesEditingRequestFull.setComment(requestFromFeatureEditFull.getComment());
            request2LocationFeaturesEditingRequestFull.setDeclineComment(requestFromFeatureEditFull.getDeclineComment());
            request2LocationFeaturesEditingRequestFull.setCreated(requestFromFeatureEditFull.getCreated());
            request2LocationFeaturesEditingRequestFull.setUser(requestFromFeatureEditFull.getUser());
            request2LocationFeaturesEditingRequestFull.setStatus(requestFromFeatureEditFull.getStatus());
            request2LocationFeaturesEditingRequestFull.setChangeSource(requestFromFeatureEditFull.getChangeSource());
            request2LocationFeaturesEditingRequestFull.setFeatureEdits(setOfOneFeature(featureEdit));
            result.add(request2LocationFeaturesEditingRequestFull);
        }
        return result;
    }

    private Set<FeatureEditFull> setOfOneFeature(FeatureEditFull feature) {
        FeatureEditFull feature2Set = new FeatureEditFull();
        feature2Set.setId(feature.getId());
        feature2Set.setAction(feature.getAction());
        feature2Set.setTc(feature.getTc());
        feature2Set.setNewValue(feature.getNewValue());
        return Collections.singleton(feature2Set);
    }

    @Override
    public List<LocationFeaturesEditingRequestFull> requestsByLocation(Integer locationId) {
        log.info("->GET /api/features-requests/::{}", locationId);
        log.info("<- GET /api/features-requests/::{}", locationId);
        return repositoryFeaturesRequests.findAllByLocationIdOrderByCreatedDesc(locationId);
    }

    @Override
    public Page<LocationFeaturesEditingRequestFull> requestsByUser(Pageable pageable, User user) {
        log.info("->GET /api/features-requests/by-user");
        log.info("<- GET /api/features-requests/by-user");
        List<Location> locationsByUser =
                repositoryAccount.findById(user.getId()).orElseThrow(NotFoundException::new)
                        .getLocations().stream().map(l ->
                                repositoryLocation.get(l.getId())).collect(Collectors.toList());
        return repositoryFeaturesRequests.findByLocation_IdIn(locationsByUser, pageable);
    }

    @Override
    public void acceptRequest(LocationFeaturesEditingRequest request, User user) {
        log.info("->GET /api/features-requests/{request}/accept");
        log.info("<- GET /api/features-requests/{request}/accept");
        request.accept(serviceWritableTc);
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }

    @Override
    public void declineRequest(LocationFeaturesEditingRequest request,
                               String comment,
                               User user) {
        log.info("->GET /api/features-requests/{request}/decline");
        log.info("<- GET /api/features-requests/{request}/decline");
        request.decline(comment);
        repositoryLocationFeaturesRequests.save(request);
        locationService.refreshCache();
    }
}
