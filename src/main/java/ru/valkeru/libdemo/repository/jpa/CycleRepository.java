package ru.valkeru.libdemo.repository.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Collection;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    @Query("""
            select new CycleDto(
                c.id,
                c.name
            ) from Cycle c
            """
    )
    Collection<CycleDto> getCycles(Sort sort);

    int deleteCycleById(Long id);

    @Query("select count(b.id) from Cycle c inner join Book b where c.id = :cycleId")
    int countBooksByCycleId(Long cycleId);

    @Query("select count(s.id) from Cycle c inner join Series s where c.id = :cycleId")
    int countSeriesByCycleId(Long cycleId);
}
