package cl.mdr.ifrs.vo;



import java.util.List;


public class DependenciaVO {
    
    private Long idGrilla;
    private List<FilaCeldaVO> filaCeldaVO;
    
    
    public DependenciaVO() {
        super();
    }

    public void setIdGrilla(Long idGrilla) {
        this.idGrilla = idGrilla;
    }

    public Long getIdGrilla() {
        return idGrilla;
    }


    public void setFilaCeldaVO(List<FilaCeldaVO> filaCeldaVO) {
        this.filaCeldaVO = filaCeldaVO;
    }

    public List<FilaCeldaVO> getFilaCeldaVO() {
        return filaCeldaVO;
    }
}
