package com.jedi.oneplacement.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class NotifMessage {
    private String title;
    private String body;
}
