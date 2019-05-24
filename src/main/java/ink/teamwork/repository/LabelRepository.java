package ink.teamwork.repository;

import ink.teamwork.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LabelRepository extends JpaRepository<Label, Long>, JpaSpecificationExecutor<Label> {

}
