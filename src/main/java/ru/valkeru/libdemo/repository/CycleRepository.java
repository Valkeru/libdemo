package ru.valkeru.libdemo.repository;

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
}
