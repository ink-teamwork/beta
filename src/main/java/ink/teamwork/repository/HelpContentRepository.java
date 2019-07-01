package ink.teamwork.repository;

import ink.teamwork.entity.HelpContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HelpContentRepository extends JpaRepository<HelpContent, Long>, JpaSpecificationExecutor<HelpContent> {
}
