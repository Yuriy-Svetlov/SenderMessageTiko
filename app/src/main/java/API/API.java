package API;

/**
 * Created by Юрий on 26.07.2015.
 */
public class API {


  private String url_create_account = "http://tikofind.esy.es/api/create_account.php";

  private String url_sing_in = "http://tikofind.esy.es/api/sing_in.php";








  public String getUrlCreateAccount(){
   return url_create_account;
  }
  public String getUrlSingIn(){
   return url_sing_in;
  }




}
