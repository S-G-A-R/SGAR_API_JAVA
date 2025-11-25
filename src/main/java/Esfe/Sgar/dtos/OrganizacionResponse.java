package Esfe.Sgar.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrganizacionResponse {
    private List<OrganizacionDto> items;
}