package com.kimhok.tickets.entity;

import com.kimhok.tickets.common.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(name = "start_date",nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date",nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private String venue;
    @Column(name = "sale_start")
    private LocalDateTime saleStart;
    @Column(name = "sale_end")
    private LocalDateTime saleEnd;
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToMany(mappedBy = "attendingEvents")
    private List<User> attendees = new ArrayList<>();

    @ManyToMany(mappedBy = "staffEvents")
    private List<User> staff = new ArrayList<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    List<TicketType> ticketTypes = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(startDate, event.startDate) && Objects.equals(endDate, event.endDate) && Objects.equals(venue, event.venue) && Objects.equals(saleStart, event.saleStart) && Objects.equals(saleEnd, event.saleEnd) && status == event.status && Objects.equals(createdAt, event.createdAt) && Objects.equals(updatedAt, event.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, venue, saleStart, saleEnd, status, createdAt, updatedAt);
    }
}