package ink.teamwork.repository;

import ink.teamwork.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {
}
