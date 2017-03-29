package es.flaviojmend.xjet.persistence.repo;

import es.flaviojmend.xjet.persistence.entity.ToughJetFlight;
import org.springframework.data.repository.CrudRepository;

public interface ToughJetRepository extends CrudRepository<ToughJetFlight, String> {

}