package br.com.brainsflow.projetoctr.entities;

/**
 * Created by marcos on 02/03/18.
 */

public class EDefinition {

    private String name;
    private String temperaturaAr;
    private String datashow;
    private String datashowOrientation;
    private String datashowResolution;
    private String computer;

    public String toString(){
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperaturaAr() {
        return temperaturaAr;
    }

    public void setTemperaturaAr(String temperaturaAr) {
        this.temperaturaAr = temperaturaAr;
    }

    public String getDatashow() {
        return datashow;
    }

    public void setDatashow(String datashow) {
        this.datashow = datashow;
    }

    public String getDatashowOrientation() {
        return datashowOrientation;
    }

    public void setDatashowOrientation(String datashowOrientation) {
        this.datashowOrientation = datashowOrientation;
    }

    public String getDatashowResolution() {
        return datashowResolution;
    }

    public void setDatashowResolution(String datashowResolution) {
        this.datashowResolution = datashowResolution;
    }

    public String getComputer() {
        return computer;
    }

    public void setComputer(String computer) {
        this.computer = computer;
    }
}