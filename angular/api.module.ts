import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { Configuration } from './configuration';
import { HttpClient } from '@angular/common/http';


import { AdminControllerService } from './api/adminController.service';
import { ApiAppealImplService } from './api/apiAppealImpl.service';
import { ApiEsiaService } from './api/apiEsia.service';
import { ApiFeaturesRequestsImplService } from './api/apiFeaturesRequestsImpl.service';
import { ApiGeoService } from './api/apiGeo.service';
import { ApiGovProgramsService } from './api/apiGovPrograms.service';
import { ApiImportService } from './api/apiImport.service';
import { ApiInternetAccessTypeService } from './api/apiInternetAccessType.service';
import { ApiLocationService } from './api/apiLocation.service';
import { ApiLocationDetailImplService } from './api/apiLocationDetailImpl.service';
import { ApiLocationFeaturesImplService } from './api/apiLocationFeaturesImpl.service';
import { ApiMapAccessPointsImplService } from './api/apiMapAccessPointsImpl.service';
import { ApiMapLocationsImplService } from './api/apiMapLocationsImpl.service';
import { ApiMobileTypeService } from './api/apiMobileType.service';
import { ApiOperatorService } from './api/apiOperator.service';
import { ApiOrganizationService } from './api/apiOrganization.service';
import { ApiOrganizationTypeService } from './api/apiOrganizationType.service';
import { ApiReportsService } from './api/apiReports.service';
import { ApiSmoTypeService } from './api/apiSmoType.service';
import { ApiTypeSimpleService } from './api/apiTypeSimple.service';
import { ApiTypeTrunkChannelService } from './api/apiTypeTrunkChannel.service';
import { ApiUserService } from './api/apiUser.service';
import { AuthApiService } from './api/authApi.service';
import { BaseStationControllerImplService } from './api/baseStationControllerImpl.service';
import { BasicErrorControllerService } from './api/basicErrorController.service';
import { FeaturesComparingApiImplService } from './api/featuresComparingApiImpl.service';
import { OperationHandlerService } from './api/operationHandler.service';
import { TrunkChannelControllerImplService } from './api/trunkChannelControllerImpl.service';
import { WebMvcLinksHandlerService } from './api/webMvcLinksHandler.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: [
    AdminControllerService,
    ApiAppealImplService,
    ApiEsiaService,
    ApiFeaturesRequestsImplService,
    ApiGeoService,
    ApiGovProgramsService,
    ApiImportService,
    ApiInternetAccessTypeService,
    ApiLocationService,
    ApiLocationDetailImplService,
    ApiLocationFeaturesImplService,
    ApiMapAccessPointsImplService,
    ApiMapLocationsImplService,
    ApiMobileTypeService,
    ApiOperatorService,
    ApiOrganizationService,
    ApiOrganizationTypeService,
    ApiReportsService,
    ApiSmoTypeService,
    ApiTypeSimpleService,
    ApiTypeTrunkChannelService,
    ApiUserService,
    AuthApiService,
    BaseStationControllerImplService,
    BasicErrorControllerService,
    FeaturesComparingApiImplService,
    OperationHandlerService,
    TrunkChannelControllerImplService,
    WebMvcLinksHandlerService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
