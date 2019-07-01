package ink.teamwork.repository;

import ink.teamwork.entity.HelpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HelpTypeRepository extends JpaRepository<HelpType, Long>, JpaSpecificationExecutor<HelpType> {

    HelpType findByNameEquals(String name);

}
