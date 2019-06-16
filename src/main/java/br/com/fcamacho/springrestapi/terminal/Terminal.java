package br.com.fcamacho.springrestapi.terminal;

import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Terminal {

    @Id
    @NotNull
    private Integer logic;

    @NotEmpty
    private String serial;

    @NotEmpty
    private String model;

    @Min(0)
    private Integer sam;

    private String ptid;

    private Integer plat;

    @NotEmpty
    private String version;

    private Integer mxr;

    private Integer mxf;

    private String verfm;

    public Integer getLogic() {
        return logic;
    }

    public void setLogic(Integer logic) {
        this.logic = logic;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSam() {
        return sam;
    }

    public void setSam(Integer sam) {
        this.sam = StringUtils.isEmpty(sam) ? null : sam;
    }

    public String getPtid() {
        return ptid;
    }

    public void setPtid(String ptid) {
        this.ptid = StringUtils.isEmpty(ptid) ? null : ptid;
    }

    public Integer getPlat() {
        return plat;
    }

    public void setPlat(Integer plat) {
        this.plat = plat;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getMxr() {
        return mxr;
    }

    public void setMxr(Integer mxr) {
        this.mxr = mxr;
    }

    public Integer getMxf() {
        return mxf;
    }

    public void setMxf(Integer mxf) {
        this.mxf = mxf;
    }

    public String getVerfm() {
        return verfm;
    }

    public void setVerfm(String verfm) {
        this.verfm = StringUtils.isEmpty(verfm) ? null : verfm;
    }
}
