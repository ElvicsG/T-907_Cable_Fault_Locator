package net.kehui.www.t_907_origin.entity;

import java.io.Serializable;

/**
 * @author jwj
 * @date 2019/12/2
 */
public class ParamInfo implements Serializable {

    private String cableVop;
    private String cableLength;
    private String cableId;
    private boolean testLead;
    private String length;
    private String vop;
    private int unit;
    /**
     * 存储的波宽度数值
     */
    private int pulseWidth;

    public String getCableVop() {
        return cableVop;
    }

    public void setCableVop(String cableVop) {
        this.cableVop = cableVop;
    }

    public String getCableLength() {
        if (cableLength == null) {
            return "0";
        }
        return cableLength;
    }

    public void setCableLength(String cableLength) {
        this.cableLength = cableLength;
    }

    public String getCableId() {
        return cableId;
    }

    public void setCableId(String cableId) {
        this.cableId = cableId;
    }

    public Boolean getTestLead() {
        return testLead;
    }

    public void setTestLead(Boolean testLead) {
        this.testLead = testLead;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getVop() {
        return vop;
    }

    public void setVop(String vop) {
        this.vop = vop;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getPulseWidth() {
        return pulseWidth;
    }

    public void setPulseWidth(int pulseWidth) {
        this.pulseWidth = pulseWidth;
    }

}
