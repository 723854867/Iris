package com.busap.vcs.webcomn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by djyin on 7/22/2014.
 */
public class SpringDataJPAPageAdapter extends XmlAdapter<SpringDataJPAPageAdapter.SpringDataJPAPageConverter, Page> {

    @Override
    public Page unmarshal(SpringDataJPAPageConverter page) throws Exception {
        return page.getPageImpl();
    }

    @Override
    public SpringDataJPAPageConverter marshal(Page v) throws Exception {
        if (v instanceof PageImpl) {
            return new SpringDataJPAPageConverter((PageImpl) v);
        } else {
            PageImpl p = new PageImpl(v.getContent());
            return new SpringDataJPAPageConverter(p);
        }
    }

    @XmlType(name = "SpringDataJPAPageConverter")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SpringDataJPAPageConverter {

        @XmlTransient
        private PageImpl pageImpl = null;

        public SpringDataJPAPageConverter() {
            super();
        }

        public SpringDataJPAPageConverter(PageImpl pageImpl) {
            this.pageImpl = pageImpl;
        }

        public PageImpl getPageImpl() {
            return pageImpl;
        }

        public void setPageImpl(PageImpl pageImpl) {
            this.pageImpl = pageImpl;
        }


    }

}

