package ch.epfl.sweng.eventmanager.repository;

import android.arch.lifecycle.LiveData;
import ch.epfl.sweng.eventmanager.repository.data.JoinedEvent;
import ch.epfl.sweng.eventmanager.repository.room.daos.JoinedEventDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JoinedEventRepository extends AbstractEventRepository<JoinedEvent, JoinedEventDao, Integer> {
    @Inject
    public JoinedEventRepository(JoinedEventDao joinedEventDao) {
        super(joinedEventDao);
    }

    public LiveData<JoinedEvent> findByName(String name){
        return dao.findByName(name);
    }
}
