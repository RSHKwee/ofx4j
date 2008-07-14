package net.sf.ofx4j.client.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * @author Ryan Heaton
 */
@XmlRootElement ( name = "institutions" )
public class FIDataList {

  private Collection<BaseFinancialInstitutionData> data;

  @XmlElement ( name = "institution")
  public Collection<BaseFinancialInstitutionData> getData() {
    return data;
  }

  public void setData(Collection<BaseFinancialInstitutionData> data) {
    this.data = data;
  }
}
