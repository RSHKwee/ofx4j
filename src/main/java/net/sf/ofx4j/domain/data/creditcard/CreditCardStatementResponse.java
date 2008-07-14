package net.sf.ofx4j.domain.data.creditcard;

import net.sf.ofx4j.domain.data.common.StatementResponse;
import net.sf.ofx4j.meta.Aggregate;
import net.sf.ofx4j.meta.ChildAggregate;

/**
 * @author Ryan Heaton
 */
@Aggregate ("CCSTMTRS")
public class CreditCardStatementResponse extends StatementResponse {

  private CreditCardAccountDetails account;

  public String getResponseMessageName() {
    return "credit card statement";
  }

  /**
   * The account for the statement.
   *
   * @return The account for the statement.
   */
  @ChildAggregate ( name ="BANKACCTFROM", order = 10)
  public CreditCardAccountDetails getAccount() {
    return account;
  }

  /**
   * The account for the statement.
   *
   * @param account The account for the statement.
   */
  public void setAccount(CreditCardAccountDetails account) {
    this.account = account;
  }

}