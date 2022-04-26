
package fr.leblanc.gomoku.repository;

import org.springframework.stereotype.Repository;
import fr.leblanc.gomoku.model.Settings;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface SettingsRepository extends CrudRepository<Settings, Long>
{
}