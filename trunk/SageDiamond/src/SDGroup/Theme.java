/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author SBANTA
 */
public class Theme {

    private static String BlueColor = "0x6699";
    private static String OrangeColor = "0xCC9900";
    private static String BlueHighlighter="plugins\\PloxeeTV\\Images\\ListView\\Highlight_blue.png";
    private static String OrangeHighlighter="plugins\\PloxeeTV\\Images\\ListView\\Highlight_orange.png";
    private static String Color = "";
    private static String Highlighter="";
    private static String ThemeProperty = SortMethods.PropertyPrefix+"/Theme";
    public static String DefaultThemeFolder = "plugins\\PloxeeTV\\Theme\\";
    public static HashMap<String,ThemeObject> Themes = new HashMap<String,ThemeObject>();
    public static ThemeObject DefaultTheme = new ThemeObject();



   public static void SetTheme(){
   String Property = GetThemeName();
   DefaultTheme = SetToThemeObject("Blue");
   if(!Property.equals("Blue")){
   Themes.put(sagex.api.Global.GetUIContextName(),SetToThemeObject(Property));}

   else{
    Themes.put(sagex.api.Global.GetUIContextName(),DefaultTheme);}

   }

  public static File GetWallBackground(){
  File curr = GetTheme().getWallBackground();
  if(!curr.exists()){
  return DefaultTheme.getWallBackground();}
  return curr; }


  public static File GetPosterBackground(){
  File curr = GetTheme().getPosterBackground();
  if(!curr.exists()){
  return DefaultTheme.getPosterBackground();}
  return curr;
  }
  public static File GetFlowBackground(){
   File curr = GetTheme().getFlowBackground();
  if(!curr.exists()){
  return DefaultTheme.getFlowBackground();}
  return curr; }

  public static File GetListBackground(){
   File curr = GetTheme().getListBackground();
  if(!curr.exists()){
  return DefaultTheme.getListBackground();}
  return curr;
  }
  public static File GetListHighlighter(){
   File curr = GetTheme().getListHighlighter();
  if(!curr.exists()){
  return DefaultTheme.getListHighlighter();}
  return curr;
  }
  public static File GetListLines(){
        File curr = GetTheme().getListLines();
  if(!curr.exists()){
  return DefaultTheme.getListLines();}
  return curr;
  }
  public static File GetBannerOverlay(){
        File curr = GetTheme().getBannerOverlay();
  if(!curr.exists()){
  return DefaultTheme.getBannerOverlay();}
  return curr;
  }
  public static File GetPosterDiffuse(){
        File curr = GetTheme().getPosterDiffuse();
  if(!curr.exists()){
  return DefaultTheme.getPosterDiffuse();}
  return curr;
  }
  public static File GetNoPoster(){
        File curr = GetTheme().getNoPoster();
  if(!curr.exists()){
  return DefaultTheme.getNoPoster();}
  return curr;
  }
  public static File GetPosterOverlay(){
        File curr = GetTheme().getPosterOverlay();
  if(!curr.exists()){
  return DefaultTheme.getPosterOverlay();}
  return curr;
  }
  public static File GetPosterFocused(){
        File curr = GetTheme().getPosterFocused();
  if(!curr.exists()){
  return DefaultTheme.getPosterFocused();}
  return curr;
  }
  public static File GetPosterNoFocus(){
      File curr = GetTheme().getPosterNoFocus();
  if(!curr.exists()){
  return DefaultTheme.getPosterNoFocus();}
  return curr;

  }
  public static File GetItemDetailBG(){
        File curr = GetTheme().getItemDetailBG();
  if(!curr.exists()){
  return DefaultTheme.getItemDetailBG();}
  return curr;
  }
  public static File GetOffButton(){
        File curr = GetTheme().getOffButton();
  if(!curr.exists()){
  return DefaultTheme.getOffButton();}
  return curr;
  }
  public static File GetOnButton(){
        File curr = GetTheme().getOnButton();
  if(!curr.exists()){
  return DefaultTheme.getOnButton();}
  return curr;
  }
  public static File GetOptionFocus(){
        File curr = GetTheme().getOptionFocus();
  if(!curr.exists()){
  return DefaultTheme.getOptionFocus();}
  return curr;
  }
  public static File GetBlackFader(){
        File curr = GetTheme().getBlackFader();
  if(!curr.exists()){
  return DefaultTheme.getBlackFader();}
  return curr;
  }
  public static File GetThumbShadow(){
        File curr = GetTheme().getThumbShadow();
  if(!curr.exists()){
  return DefaultTheme.getThumbShadow();}
  return curr;
  }
  public static File GetOptionsBG(){
        File curr = GetTheme().getOptionsBG();
  if(!curr.exists()){
  return DefaultTheme.getOptionsBG();}
  return curr;
  }
  public static File GetMainBackground(){
      File curr = GetTheme().getMainBackground();
  if(!curr.exists()){
  return DefaultTheme.getMainBackground();}
  return curr;
  }

 public static String GetThemeColor(){
 return GetTheme().getThemeColor();}

 public static String GetThemeHeaderFont(){
 return GetTheme().getThemeHeaderFont();
 }

 public static String GetThemeFont(){
 return GetTheme().getThemeFont();}



   private static ThemeObject SetToThemeObject(String Theme){
   ThemeObject theme= new ThemeObject();
   String ThemeDir = DefaultThemeFolder+Theme+"\\";
   //Flow View
   String FlowDirectory = ThemeDir+"FlowView\\";
   theme.setFlowBackground(new File(FlowDirectory+"background_flow.png"));

   //List View
   String ListDirectory = ThemeDir+"ListView\\";
   theme.setListBackground(new File(ListDirectory+"background_list.png"));
   theme.setListHighlighter(new File(ListDirectory+"highlight.png"));
   theme.setListLines(new File(ListDirectory+"lines.png"));

     //Poster View
   String PosterDirectory = ThemeDir+"PosterView\\";
   theme.setPosterBackground(new File(PosterDirectory+"background_poster.png"));

     //Transformations
   String TransformationsDirectory = ThemeDir+"Transformations\\";
   theme.setBannerOverlay(new File(TransformationsDirectory+"banner_overlay.png"));
   theme.setNoPoster(new File(TransformationsDirectory+"no_poster.png"));
   theme.setPosterDiffuse(new File(TransformationsDirectory+"poster_diffuse.png"));
   theme.setPosterFocused(new File(TransformationsDirectory+"poster_focus.png"));
   theme.setPosterNoFocus(new File(TransformationsDirectory+"poster_nofocus.png"));
   theme.setPosterOverlay(new File(TransformationsDirectory+"poster_overlay.png"));


     //Universal
   String UniversalDirectory = ThemeDir+"Universal\\";
   theme.setBlackFader(new File(UniversalDirectory+"black_fader.png"));
   theme.setItemDetailBG(new File(UniversalDirectory+"item_detail_bg.png"));
   theme.setMainBackground(new File(UniversalDirectory+"main_background.png"));
   theme.setOffButton(new File(UniversalDirectory+"off_button.png"));
   theme.setOnButton(new File(UniversalDirectory+"on_button.png"));
   theme.setOptionFocus(new File(UniversalDirectory+"options_focus.png"));
   theme.setOptionsBG(new File(UniversalDirectory+"options_bg.png"));
   theme.setThumbShadow(new File(UniversalDirectory+"thumb_shadow.png"));

     //WallView
   String WallDirectory = ThemeDir+"WallView\\";
   theme.setWallBackground(new File(WallDirectory+"background_wall.png"));

   //properties
   theme.setThemeColor(ThemeProperties.GetProperty("ThemeColor","0x6699"));
   theme.setThemeFont(ThemeProperties.GetProperty("ThemeFont","plugins//PloxeeTV//Font//AltDefault"));
   theme.setThemeHeaderFont(ThemeProperties.GetProperty("ThemeHeaderFont","plugins//PloxeeTV//Font//DefaultCaps"));

   return theme;

   }

   public static String GetThemeName(){
   return  sagex.api.Configuration.GetProperty(ThemeProperty,"Blue");}

   public static ThemeObject GetTheme(){
   return Themes.get(sagex.api.Global.GetUIContextName());}


}