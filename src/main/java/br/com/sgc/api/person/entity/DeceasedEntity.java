package br.com.sgc.api.person.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.sgc.api.burial.entity.BurialEntity;
import br.com.sgc.api.common.enums.DeceasedStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "deceased")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class DeceasedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dec_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dec_status")
    private DeceasedStatus status;

    @Column(name = "dec_archived")
    private boolean archived;

    @Column(name = "dec_archived_at")
    private LocalDateTime archivedAt;

    @OneToMany(mappedBy = "deceased", fetch = FetchType.LAZY)
    private List<BurialEntity> burials = new ArrayList<>();
}
