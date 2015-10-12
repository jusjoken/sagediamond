# FAQ #

Look here first to see if your question has already been answered.


## Q. Where has my Imported TV gone? ##
### A. It's now under TV - Recordings - All Recordings etc... ###
> In TV you still have the default Sagetv options to create new Recording Views. There are 4 by default (All Recordings, Current Recordings, Archived Recordings, and Recorded Movies)

> From the main menu go to TV/Recordings/All Recordings and select. Then hit escape (or option button) and then go to Menu Options, change the "Number of Views" to 5. Go all the way back out to the main menu and the go to the new TV/Recordings/"Recording View 5" and select. Hit escape (or option button) then go back to Menu Options, you will now see an option to rename the "recording view 5" go ahead and rename it "Imported TV". Exit back to the new "Imported TV" view. Go into options again and this time select "Filtering" and then change the Imported TV option to be "Only Imported TV", you should now have all "Imported TV" showing in your own custom TV view that uses the Banners or Simplified Banner view.

> Or if you don't use one of the others you can rename it and just adjust the filters to only show the Imported TV.

> [Wiki info here](http://code.google.com/p/sagediamond/wiki/DiamondInformation#TV/Recordings_Views)

## Q. Where is the TV Main Menu Item? ##
### A. Make sure you are using the SageTV 7.1.x Beta or... ###
  * make sure the TV menu item is turned on in Diamond Options - [Wiki](http://code.google.com/p/sagediamond/wiki/DiamondInformation#Main_Menu_Mods)
  * make sure you are running SageTV 7.1.x beta or above
  * make sure you have all other Menu Mod type plugins disabled or uninstalled
  * try a quick restart of SageTV (server, client etc)

## Q. Where are my Recorded Movies? ##
### A. You have some choices... check here.... ###
> If you are using Diamond v3 it filters recorded movies out of recordings and into the Movies/Video views by default as the TV views do not lend well to displaying Movie information but you have options....
  * Use Videos and then one of the 'Diamond' views such as Wall, Flow, List Flow, or Categories [Wiki](http://code.google.com/p/sagediamond/wiki/DiamondInformation#General_Options)
  * Switch to the Diamond 'Movie'view and then create a custom Wall, Flow, or List Flow View [Wiki](http://code.google.com/p/sagediamond/wiki/DiamondInformation#Main_Menu_Mods)
  * Show Recorded Movies in the TV/Recording Views
    * look at the Filters in the Options panel of the Recorded Movies View (or other Recording Views).  Make sure that Recorded Movies is filtered to be included. [Wiki](http://code.google.com/p/sagediamond/wiki/DiamondInformation#Standard_Diamond_TV/Recordings_View)

## Q. I am missing some Movies or Videos.  How do I get them to show? ##
### A. Check the setting for "unscraped" content ###
  * From one of the Diamond Movie views go into Options and select Filters
  * Make sure "Include Unscraped Content" is set to "On"
  * This will include content where metadata does not exist from sources like BMT. For example: mymovies or home videos.
  * **Note:** This will only affect the current diamond Movie view.

## Q. Can I use Diamond on an HDxxx Extender? ##
### A. Absolutely ###
  * Testing and user reports indicate Diamond functions well on HD200 and HD300 extenders. Some users have indicated "Ok" performance on HD100's with much of the fanart options disabled. See [this SageTV thread](http://forums.sagetv.com/forums/showthread.php?p=488552&postcount=1) for more info.
  * Diamond versions 3.10 and 3.20 added some options that may improve performance on some extenders such as "Less Posters" on Wall Flow, the new "360 Flow" in 3.20, the Option to hide the Poster in the Diamond Info View in TV/Recordings as well as in the Movie List Flow you can disable the center Poster scroll list.

## Q. Can I use "Custom" TV views in Diamond? ##
### A. Sure - but this is actually a default SageTV feature - JOrton explains below... ###
  * The default Sagetv has this already built into it. In the main menu if you go TV/Recordings/ you should see "All Recordings", "Current Recordings", "Archived Recordings" and "Recorded Movies" setup by default. When in any of these TV views if you hit esc or "option" button then pick "menu options" you will see some options for the TV views at the bottom of the pop up, like number of views and Rename the "xxxxxx" view.
  * By default these 4 views are setup but you can have more than this or rename and repurpose them for other uses like "Kid's TV" etc... and then modify the filtering on each view to only include the shows you would like to see, of course this means that the shows have to have enough metadata etc to allow them to be filtered from the rest of your shows.
  * There currently isn't "folder" level filtering like in the move views so you are limited to using the filters that are availible be default plus any that we added as part of Diamond like the "recorded movies", "imported tv from playon" and "Imported TV" filters. Under catagories you can use the default catagories and the custom "user catagories".
  * I have my "Archived Recordings" set to only include "Imported TV" so I have all my non recorded shows there and then I renamed it "Imported TV". Based on the number of filter options, catagory filtering and the use of custom user catagories (Sagetv feature) you can do quite a bit.
