package io.github.starrybleu.sideproject0.api.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateAllocationRequestReqBody {
    @Size(min = 1, max = 100)
    String address;
}
