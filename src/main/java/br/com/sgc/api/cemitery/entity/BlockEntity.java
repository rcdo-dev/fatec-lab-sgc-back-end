    package br.com.sgc.api.cemitery.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;

    import lombok.Data;

    @Entity
    @Table(name = "block")
    @Data
    public class BlockEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "blo_id", nullable = false)
        private Long id;

        @Column(name = "blo_number", nullable = false)
        private int number;

        @Column(name = "blo_description")
        private String description;

        /**
         * ManyToOne = dono da relação (Sempre tem a FK).
         * Responsável por gravar a relação no banco de dados
         */
        @ManyToOne
        @JoinColumn(name = "fk_blo_cem_id", nullable = false)
        private CemeteryEntity cemetery;
    }