package io.queberry.que.appointment;

import io.queberry.que.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,String> {
//    Set<Appointment> findByMobile(String mobile, Sort sort);
//
//    Appointment findByMobileAndCheckinCodeAndState(String mobile, String checkinCode, Appointment.State state);
//
//    Set<Appointment> findByDateAndFromAndToAndStateAndServiceId(LocalDate date, LocalTime from, LocalTime to, Appointment.State state, String s);
//
//    Set<Appointment> findByDateAndFromAndToAndMobile(LocalDate date, LocalTime from, LocalTime to, String mobile);
//
//    Set<Appointment> findByStateAndDate(Appointment.State state, LocalDate date);

    Page<Appointment> findByServiceInAndDateAndStateIn(Set<Service> services, LocalDate cDate, Set<Appointment.State> state, Pageable pageable);

//   // Set<Appointment> findByDateBetweenAndBranchAndServiceIdInAndStateIsInAndSchedulingTypeIn(LocalDate sdate, LocalDate edate, String b, Set<String> s, Set<Appointment.State> states, Set<Appointment.SchedulingType> schedules);
//
//    Set<Appointment> findByBranchAndServiceIdAndDateAndMobile(String b, String s, LocalDate date, String mobile);
//
//    Optional<Appointment> findByCheckinCodeAndDate(String checkinCode, LocalDate d);
//
//    Optional<Appointment> findByCheckinCodeAndBranch(String check, String b);
//
//    Optional<Appointment> findByAssistanceidAndBranch(String id, String b);
//
//    Set<Appointment> findByBranch(String branchKey);

//    Set<Appointment> findByMobileAndAppointmentType(String m, Appointment.AppointmentType appointmentType);
//    Set<Appointment> findByMobileAndCheckinCodeIsNotNull(String mobile);
//    Set<Appointment> findByServiceAndDateAndStateAndFromAndTo(Service s, LocalDate date, Appointment.State state, LocalTime from, LocalTime to);
//
//    /* Below two repositories to check duplicate appointments */
//    Set<Appointment> findByServiceAndDateAndMobile(Service s, LocalDate date, String mobile);
//
//    Page<Appointment> findByDateBetweenAndServiceIn(LocalDate sdate, LocalDate edate, Set<Service> s, Pageable pageable);
//    Page<Appointment> findByDateBetweenAndServiceInAndAndTypeofAppointmentIsInAndStateIsIn(LocalDate sdate, LocalDate edate, Set<Service> s, Set<Appointment.ModeofAppointment> type,Set<Appointment.State> states,Pageable pageable);
////    Set<Appointment> findBySmsOtpContaining(String refno);
//    Set<Appointment> findByDateBetweenAndBranchAndServiceInAndTypeofAppointmentIsInAndStateIsInAndSchedulingTypeIn(LocalDate sdate, LocalDate edate, String b,Set<Service> s,Set<Appointment.ModeofAppointment> type,Set<Appointment.State> states,Set<Appointment.SchedulingType> schedules);
//    Set<Appointment> findByDateBetweenAndBranchAndServiceInAndStateIsInAndSchedulingTypeIn(LocalDate sdate, LocalDate edate, String b,Set<Service> s,Set<Appointment.State> states,Set<Appointment.SchedulingType> schedules);
//    // Set<Appointment> findByDateBetweenAndBranchInAndServiceInAndTypeofAppointmentIsInAndStateIsInAndSchedulingTypeIsIn(LocalDate sdate, LocalDate eDate,Set<String> b,Set<Service> s,Set<Appointment.ModeofAppointment> type,Set<Appointment.State> states,Set<Appointment.SchedulingType> stype);
//    Set<Appointment> findByBranchAndServiceAndDateAndMobile(String b, Service s, LocalDate date, String mobile);
//
////    @Query("SELECT a, b.name FROM Appointment a INNER JOIN Branch b where a.branch = b.branchKey and a.date >= :start and a.date <= :end and a.service in :service and a.branch in :branch and a.state in :state and a.typeofAppointment in :mode and a.schedulingType in :scheduling")
////    Set<AppointmentBranchName> findByDateBetweenAndBranchInAndServiceInAndTypeofAppointmentIsInAndStateIsInAndSchedulingTypeIsIn(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("branch") Set<String> branch, @Param("service") Set<Service> service, @Param("mode") Set<Appointment.ModeofAppointment> type, @Param("state") Set<Appointment.State> states,@Param("scheduling") Set<Appointment.SchedulingType> scheduling);
//
//    @Query(value = "SELECT a.*, b.name FROM appointment a JOIN branch b ON a.branch = b.branch_key WHERE a.date >= :start AND a.date <= :end AND a.service_id IN :service AND a.branch IN :branch AND a.state IN :state AND a.typeof_appointment IN :mode AND a.scheduling_type IN :scheduling", nativeQuery = true)
//    Set<AppointmentBranchName> findByDateBetweenAndBranchInAndServiceInAndTypeofAppointmentIsInAndStateIsInAndSchedulingTypeIsIn(
//            @Param("start") LocalDate start,
//            @Param("end") LocalDate end,
//            @Param("branch") Set<String> branch,
//            @Param("service") Set<Service> service,
//            @Param("mode") Set<Appointment.ModeofAppointment> type,
//            @Param("state") Set<Appointment.State> states,
//            @Param("scheduling") Set<Appointment.SchedulingType> scheduling
//    );
//
//    @Query(value = "SELECT a, b.name FROM Appointment a JOIN Branch b ON a.branch = b.branchKey")
//    Set<AppointmentBranchName> findByBranchIsNotNull();
//
//
//    @Query("SELECT new io.queberry.que.appointment.AppointmentBranchName(a, b.name) " +
//            "FROM io.queberry.que.appointment.Appointment a " +
//            "JOIN io.queberry.que.branch.Branch b " +
//            "ON a.branch = b.branchKey " +
//            "WHERE a.smsOtp LIKE CONCAT('%', :refno, '%')")
//    Set<AppointmentBranchName> findBySmsOtpContaining(@Param("refno") String refno);
}


