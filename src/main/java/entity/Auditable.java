package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//@Getter
////@Setter
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createAt", "updatedAt"}, allowGetters = true)
//public abstract class Auditable {
//    @Id
//    @SequenceGenerator(name= "primary_key_seq")
//    private
//
//}
