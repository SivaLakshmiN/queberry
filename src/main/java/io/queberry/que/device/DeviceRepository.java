package io.queberry.que.device;
import com.google.common.base.Ticker;
import io.queberry.que.counter.Counter;
import io.queberry.que.media.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface DeviceRepository extends JpaRepository<Device, String>{

    Optional<Device> findByDeviceId(String deviceId);
    Set<Device> findByPlaylist(Playlist playlist);
    Set<Device> findByCountersAndType(Counter counter, Device.Type type);
    Page<Device> findByPairedBy(String pairedTo,Pageable pageable);

    Page<Device> findByPairedByAndType(String pairedTo,Device.Type type,Pageable pageable);
    Set<Device> findByTickers(Ticker t);
}
