package br.com.sgc.api.cemetery.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.sgc.api.common.enums.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_id", nullable = false)
	private Long id;

	@Column(name = "con_number", nullable = false)
	private String number;

	@Column(name = "con_fee", nullable = false)
	private BigDecimal fee;

	@Column(name = "con_status", nullable = false)
	private ContractStatus status;

	@Column(name = "con_start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "con_end_date", nullable = false)
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_con_conh_id", nullable = false)
	private ContractHolderEntity holder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_con_gra_id", nullable = false)
	private GraveEntity grave;
}
