package ru.cifrak.telecomit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cifrak.telecomit.backend.entities.Operator;

import java.util.List;

@Repository
public interface RepositoryOperator extends JpaRepository<Operator, Integer> {

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Интернет'")
    List<Operator> internet();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Мобильная связь'")
    List<Operator> mobile();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'АТС'")
    List<Operator> ats();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'ТВ'")
    List<Operator> television();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Радио'")
    List<Operator> radio();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Таксофон'")
    List<Operator> payphone();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Инфомат'")
    List<Operator> infomat();

    @Query("SELECT o from Operator o" +
            " JOIN FETCH o.services s" +
            " where s.label = 'Почта'")
    List<Operator> postal();

    Operator findByName(String name);
}
