package it.librone.okipo.task.DTO;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "message",
        "result"
})

public class ethScanResponseDTOv2 {

        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private String message;


        // Custom deserializer poichè può essere String o List<Result>
        @JsonDeserialize(using = CustomListDeserializer.class)
        private Object result;


        //ignoro eventuali field non necessari
       // @JsonIgnore
        private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

        @JsonProperty("status")
        public String getStatus() {
            return status;
        }

        @JsonProperty("status")
        public void setStatus(String status) {
            this.status = status;
        }

        @JsonProperty("message")
        public String getMessage() {
            return message;
        }

        @JsonProperty("message")
        public void setMessage(String message) {
            this.message = message;
        }

        @JsonProperty("result")
        public Object getResult() {
            return result;
        }

        @JsonProperty("result")
        public void setResult(Object result) {
            this.result = result;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }


}
