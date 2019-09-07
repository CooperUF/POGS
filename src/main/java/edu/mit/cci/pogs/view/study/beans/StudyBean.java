package edu.mit.cci.pogs.view.study.beans;


import edu.mit.cci.pogs.model.jooq.tables.pojos.Study;
import edu.mit.cci.pogs.view.researchgroup.beans.ResearchGroupRelationshipBean;

public class StudyBean extends Study {

    public StudyBean() {

    }

    public StudyBean(Study study) {
        super(study);
    }

    private ResearchGroupRelationshipBean researchGroupRelationshipBean;

    public ResearchGroupRelationshipBean getResearchGroupRelationshipBean() {
        return researchGroupRelationshipBean;
    }

    public void setResearchGroupRelationshipBean(ResearchGroupRelationshipBean researchGroupRelationshipBean) {
        this.researchGroupRelationshipBean = researchGroupRelationshipBean;
    }
}
