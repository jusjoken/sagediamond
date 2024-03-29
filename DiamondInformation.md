# Introduction #

Check here for basic information about what's available in Diamond and how to use various functions.

Also check out the [FAQ](http://code.google.com/p/sagediamond/wiki/DiamondFAQ?ts=1299329291&updated=DiamondFAQ#FAQ)



![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/DiamondMainMenu1-small.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/DiamondMainMenu1.jpg) _click to view_

# Details #

## New in Version 3.30 ##

  * Works with SageTV 7.1.7

  * Main Menu Widget
    * Extended Weather widget enhanced
  * TV/Recordings Views
    * Bypass Single Seasons Options added ([Issue 160](https://code.google.com/p/sagediamond/issues/detail?id=160))
    * Show minimal recording options is now a seperate option for the TV/Recording list and the episodes ([Issue 133](https://code.google.com/p/sagediamond/issues/detail?id=133))
    * Enter=Play option added to the Episode Lists
    * Simplified view enhancements
      * New option to show more rows (11 rows instead of 5)
      * Can now toggle off the New/Last/Next in the Diamond Info Panel ([Issue 157](https://code.google.com/p/sagediamond/issues/detail?id=157))
  * Movies/Video View
    * NEW - Sideways Flow view which offers a vertically scrolling wall type view
    * Cover Flow - new option to display the posters on the bottom of the screen
    * Jump to Diamond Views from any other Diamond Views using the new Diamond Views option on the Options panel
    * Info Options - new option to show the Diamond Info Fade screen when the Info button is pressed
    * Wall Flow - new option to show the title below each poster ([Issue 153](https://code.google.com/p/sagediamond/issues/detail?id=153))
    * Custom Views can now be renamed in addition to changing their type ([Issue 174](https://code.google.com/p/sagediamond/issues/detail?id=174))
  * Other
    * SageTV default icons are now themed for various media types ([Issue 180](https://code.google.com/p/sagediamond/issues/detail?id=180))
    * Fixed
      * [issue 114](https://code.google.com/p/sagediamond/issues/detail?id=114), [issue 118](https://code.google.com/p/sagediamond/issues/detail?id=118), [issue 148](https://code.google.com/p/sagediamond/issues/detail?id=148), [issue 149](https://code.google.com/p/sagediamond/issues/detail?id=149), [issue 150](https://code.google.com/p/sagediamond/issues/detail?id=150), [issue 151](https://code.google.com/p/sagediamond/issues/detail?id=151), [issue 154](https://code.google.com/p/sagediamond/issues/detail?id=154), [issue 155](https://code.google.com/p/sagediamond/issues/detail?id=155), [issue 156](https://code.google.com/p/sagediamond/issues/detail?id=156), [issue 157](https://code.google.com/p/sagediamond/issues/detail?id=157), [issue 159](https://code.google.com/p/sagediamond/issues/detail?id=159), [issue 160](https://code.google.com/p/sagediamond/issues/detail?id=160), [issue 161](https://code.google.com/p/sagediamond/issues/detail?id=161), [issue 164](https://code.google.com/p/sagediamond/issues/detail?id=164), [issue 165](https://code.google.com/p/sagediamond/issues/detail?id=165), [issue 167](https://code.google.com/p/sagediamond/issues/detail?id=167), [issue 169](https://code.google.com/p/sagediamond/issues/detail?id=169), [issue 175](https://code.google.com/p/sagediamond/issues/detail?id=175), [issue 179](https://code.google.com/p/sagediamond/issues/detail?id=179), [issue 180](https://code.google.com/p/sagediamond/issues/detail?id=180), [issue 181](https://code.google.com/p/sagediamond/issues/detail?id=181), [issue 182](https://code.google.com/p/sagediamond/issues/detail?id=182), [issue 184](https://code.google.com/p/sagediamond/issues/detail?id=184), [issue 185](https://code.google.com/p/sagediamond/issues/detail?id=185), [issue 186](https://code.google.com/p/sagediamond/issues/detail?id=186), [issue 187](https://code.google.com/p/sagediamond/issues/detail?id=187)


## New in Version 3.20 ##

  * Works with SageTV 7.1.5

  * Main Menu Widget
    * Widget Control Panel - all settings in one spot
    * this is a new approach so your old settings will be lost and you will need to set new ones in the Widget Control Panel
  * TV/Recordings Views
    * All TV/Recordings Views now save their settings (filters, fanart settings etc) independently - with this release previous settings will be lost to support this design change
    * Diamond Play Menu - Play/FF/Stop buttons can be used to select menu items (Next/New/Close)
    * Option - display Original Air Date OR Recorded Date in TV/Recording items
    * Option - toggle New/Last/Next to display or not
    * Option - include tv/recording list in Diamond Info Panel
    * Option - include S/E in tv/recording list in Diamond Info Panel or standard panel
    * Option - can disable the Series Poster in Diamond Info Panel
    * Change - S/E info in Diamond Info Panel if On now matches S/E settings in options
  * Movies/Video View
    * NEW View - 360 Flow - a category style view based on the Xbox 360 Netflix implementation
    * Category and 360 Flow Views are now part of the Custom Views and can have configurable filtering
    * Flow views (except List) allows FF/RW for horizontal type paging
    * Info Fade - new option for a right side slide out info panel (Slide Panel)
  * Other
    * Lots of changes under the hood
    * Option to rebuild all Fanart when deleting the cache
    * Instant re-cache of Series/Movie Fanart after deleting a single Fanart item
    * Many fixes (see Issues list)
    * Caching improvements (you will need to clear and rebuild your cache)
    * Read ahead caching option found in Detailed Setup - Diamond Fanart

## Getting Started ##

  * available as a "UI Mod" plugin under SageTV version 7+ called "Diamond for Default Sagetv STV"
  * to get the most out of this plugin you will need some form of metadata/Fanart solution such as...
    * Phoenix Core Services with BMT
    * other provider that store folder level fanart (posters and backgrounds). See fanart below.
  * select a Diamond theme from among the large selection available under Detailed Setup, Advanced, "select the user interface theme".
  * if you are a previous Diamond 2.0 or greater user you may want to clear all the previous settings - look here [#Troubleshooting](#Troubleshooting.md)

## Diamond Themes ##

|Diamond 	     	  |Diamond MC		      |Diamond Simplified  |
|:----------------|:-----------------|:-------------------|
|Diamond All Clear  	  |Diamond MC No Fanart BG   |Diamond Tabs	       |
|Diamond Blue Too   	  |Diamond Orange	      |Diamond Winter      |
|Diamond Boxwee     	  |Diamond Purple	      |Diamond Winter Red  |
|Diamond Boxwee EP  	  |Diamond Red		      |		                  |
|Diamond Green	     	  |Diamond Serenity Blue     |	                   |
|Diamond Green Too	  |Diamond Serenity Now      |	                   |

## TV/Recordings Views ##
![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-small.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View.jpg) _click to view_
### Standard Diamond TV/Recordings View ###
  * select TV - Recordings - All Recordings (or one of the other default SageTV recording views)
    * Creating more TV/Recording Views and/or renaming the existing views
      * Select Options and then "Menu Options"
        * **NOTE:** this is a Default SageTV function
        * Change the number of Custom TV views to show (between 4 and 8) by selecting "Number of Views"
        * Select "Rename the "Name of View you are in" View - to name the TV view something unique
    * Diamond Options - select Options and then Diamond Options - from here change...
      * Fanart Options
        * BG Fanart - on - off
        * Show Series Banner - on - off
        * Show Season Banner - on - off
        * Fanart Thumbnails - affects the default info list and episode view - on - off
        * Show Larger Thumbnails - affects the default info list and episode view - on - off
      * Series Info Options
        * Use Diamond Info View - custom right hand info panel with optional poster view and metadata details - on - off
> > > > |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfo-off-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfo-off.jpg) _Off_ |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfo-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfo.jpg) _On_ |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfoNoPoster-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfoNoPoster.jpg) _no poster_ | _click to view_ |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|
          * With Diamond Info View - OFF
            * S/E in Recording List - on - off
            * Show Larger Thumbnails - on - off
            * Fanart Thumbnails - on - off
          * With Diamond Info View - ON
            * Show Poster - on - off
            * Diamond Info Recording List - include the recording list instead of the description - on - off
              * if ON...
                * Recording List Sort Style - Oringal Air Date - Recorded Date
                * S/E in Recording List - on - off
            * Show New, Last, Next - on - off
            * New, Last, Next Shows S/E - on - off
> > > > > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfoSE-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondInfoSE.jpg) _click to view_
        * Season/Episode Format - S1E01 - S01E01 - E01 - 1x01 - 1 - Row Number - None
        * Season/Episode Separator - "-" - "::" - "." - ")" - "|" - None
      * Count/Watched Format - (www/rrr) - (rrr) - Recordings:rrr - Recordings:rrr Watched:www - Blank
      * Show Seasons View - on - off
      * Bypass Single Seasons - on - off
      * Show Diamond Play Options - if enabled this will display a menu to play Next, Newest, All from Next or All - on - off
        * press Play again to "Play Next"
        * press FFwd to "Play Newest"
        * press Stop to close the options menu

> > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondPlayOption-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-DiamondPlayOption.jpg) _click to view_
      * Season Popup - display the Season List in a popup menu - on - off
> > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-SeasonPopup-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-SeasonPopup.jpg) _click to view_
      * Show Minimal Recording Icons - on - off
        * affects TV/Recordings List separately from group items
      * Switch to Simplified View - a Series Banner driven clean approach to TV/Recordings
    * Filters - select from the following filter options....

> > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-Filters-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-Filters.jpg) _click to view_
      * Manual Recordings
      * Favorites
      * non Manual Recording or Favorite
      * archived Recordings
      * First Runs
      * HDTV Recordings
      * Watched Recordings
      * Recordings marked Don't Like
      * Recorded movies
      * Imported TV from PlayOn
      * Imported TV
      * Categories filtered in: (select this option to pick which categories to include).
  * Episode View Options - while on the episode view select Options
    * Select Diamond Options - from here change...
      * BG Fanart - on - off
      * Hide Title in title Grouping - on - off
      * Show Season/Episode Number - on - off
      * Season/Episode Format - S1E01 - S01E01 - !E01 - !1x01 - !1 - Row Number - None
      * Season/Episode Separator - "-" - "::" - "." - ")" - "|" - None
      * Season/Episode Location - Before Title - After Title - Above Markers (only works if Minimal Recording Icons = on)
      * Show Minimal Recording Icons - on - off
      * Show Airing Date
        * only available if Minimal Recording Icons = on and S/E Location is not above Markers
        * can be set to "Original Airdate" or "Recorded Date"
      * Enter=Play - pressing Enter will simulate pressing Play
    * Sort - look here to set the sort to various options including Season/Episode and Original Air Date
> > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-EpisodeSort-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Recordings-TV-View-EpisodeSort.jpg) _click to view_
### Simplified View ###

> |[http://dl.dropbox.com/u/7826058/Diamond/Recordings-TV-View-SimplifiedView-ppc.JPG](http://dl.dropbox.com/u/7826058/Diamond/Recordings-TV-View-SimplifiedView.JPG) _Default rows_                          |![![](http://dl.dropbox.com/u/7826058/Diamond/Recordings-TV-View-SimplifiedView_more-ppc.jpg)](http://dl.dropbox.com/u/7826058/Diamond/Recordings-TV-View-SimplifiedView_more.jpg) _More rows_    | _click to view_                                                                                                                                                                                                         |
  * a Series Banner driven clean approach to TV/Recordings
    * Diamond Options - select Options and then Diamond Options - from here change...
      * Fanart Options
        * BG Fanart - on - off
        * Show Poster - on - off
        * Fanart Thumbnails - affects the episode view - on - off
        * Show Larger Thumbnails - affects the episode view - on - off
      * Series Info Options
        * Show New, Last, Next - on - off
        * New, Last, Next Shows S/E - on - off
        * Season/Episode Format - S1E01 - S01E01 - !E01 - !1x01 - !1 - Row Number - None
        * Show Poster - on - off
        * Show Title on Right - on - off
        * Show Description - on - off
      * Show Seasons View - on - off
      * Show Diamond Play Options - if enabled this will display a menu to play Next, Newest, All from Next or All - on - off
        * press Play again to "Play Next"
        * press FFwd to "Play Newest"
        * press Stop to close the options menu
      * Season Popup - display the Season List in a popup menu - on - off
      * Show More Rows - will display 11 rows vs. default 5 - on - off
      * Show Minimal Recording Icons - on - off
      * Enter=Play - pressing Enter will simulate pressing Play
      * Switch to Standard View

  * **Note:** options are now saved per individual Recordings/TV view.

## Movies/Videos Flow Views ##

### Cover Flow ###
  * movie poster driven view centered on the screen for quick selection.
> |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieCoverFlowView-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieCoverFlowView.jpg) _Default_|![![](http://dl.dropbox.com/u/7826058/Diamond/MovieCoverFlowViewBottom-ppc.jpg)](http://dl.dropbox.com/u/7826058/Diamond/MovieCoverFlowViewBottom.jpg) _Posters on Bottom_| _click to view_ |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|

### Wall Flow ###
  * a wall of Movie Posters to select from.
> |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieWallView-InfoOff-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieWallView-InfoOff.jpg) _No Info_|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieWallView-InfoOn-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieWallView-InfoOn.jpg) _Show Info_| _click to view_ |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|

### List Flow ###
  * list based Movie view with a vertical scrolling poster view and Movie details on the right.
> |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieListFlowView-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieListFlowView.jpg) _Poster_ |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieListFlowView_noPoster-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieListFlowView_noPoster.jpg) _no Poster_ | _click to view_ |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|

### Category Flow ###
  * this is a Genre type view with Categories on the left and a Wall type view on the right. Use the Options menu to ........
> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieCategories-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieCategories.jpg) _click to view_

### 360 Flow ###
  * this is a Genre type view based on the Xbox 360 Netflix implementation........
> |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Movie360Flow-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Movie360Flow.jpg) _No Info_|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Movie360FlowWithInfoBoxOn-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Movie360FlowWithInfoBoxOn.jpg) _Show Info_| _click to view_ |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|

### SideWays Flow ###
  * a vertical scrolling wall of Movie Posters with movie details on the right.
> |[http://dl.dropbox.com/u/7826058/Diamond/MovieSideWaysFlow-ppc.JPG](http://dl.dropbox.com/u/7826058/Diamond/MovieSideWaysFlow.JPG) _Default view_|[http://dl.dropbox.com/u/7826058/Diamond/MovieSideWaysFlow\_lessPosters-ppc.JPG](http://dl.dropbox.com/u/7826058/Diamond/MovieSideWaysFlow_lessPosters.JPG) _Less Posters_| _click to view_ |
|:------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|


### General Options for Flow Views ###

|Option|Wall|Cover|List|Cat|360|SW|
|:-----|:---|:----|:---|:--|:--|:-|
|Folder Filters|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Filters|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Sort  |![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Enter=Play|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_pressing Enter will simulate pressing Play_|
|BG Fanart|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_toggles Background Fanart off and on_|
|ABC Jumpbar|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Show Info Box|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_show the Diamond Info panel on the right side_|
|Sub Categories|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|_include subcategories_|
|Small Fanart BG|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|_display a small window above the posters containing the background fanart_|
|Delay Movies|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|_delays the display of the posters to speed the scrolling of the categories_|
|Posters on Bottom|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|_delays the display of the posters to speed the scrolling of the categories_|
|Customize View|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/no.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Diamond Views|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Info Options|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|![http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png](http://babgvant.com/downloads/pluckyhd/DiamondImages/yes.png)|_see below_|
|Option|Wall|Cover|List|Cat|360|SW|


  * Folder Filters - here you can include or exclude specified Movie Folders
> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieFolderFilters-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MovieFolderFilters.jpg) _click to view_
    * Add New Include Filter
    * Add New Exclude Filter
    * remove/change existing Folder filters
  * Filter
    * Include Watched
    * Include DVD
    * Include BluRay
    * Include Videos
    * Include Recordings
    * Include Unscraped Content
      * this will include content where metadata does not exist from sources like BMT. For example: mymovies or home videos.
    * Include PlayOn Content
    * Categories filtered in: (select this option to pick which categories to include)
  * Sort
    * Oldest/Newest Added First
    * Title A to Z or Z to A
      * ScrubTitle - removes The, A etc - on - off
    * Release Year 1900 to Present or Present to 1900
  * ABC Jumpbar
    * Press "down" in this view to get the [ABC Jumpbar](http://babgvant.com/downloads/pluckyhd/DiamondImages/ABCScrollBar.jpg) to Jump to the first movie starting with a selected letter.
> > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/ABCScrollBar-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/ABCScrollBar.jpg) _click to view_
  * Customize View
    * List Flow
      * Right Side - what to display on the right side of the title in the movie list - Blank - Ratings - Year
      * Show Posters - turn Off to remove the verticle poster list (may help performance on slower systems/extenders)
      * Wrap List - on - off - if on the list will loop back to the begin/end
      * Show Row Number - on - off - displays the row number before the movie title
    * Wall Flow
      * Show Info Box - on - off
      * Less Posters - display less posters to improve speed - on - off
      * Show Title - display the title below each poster - on - off
    * SideWays Flow
      * Less Posters - display less posters to improve speed - on - off
      * Show Title - display the title below each poster - on - off
  * Diamond Views
    * This option will display a submenu to allow you to jump to any other Custom or Default Diamond View
    * The submenu will display an example screen for each view
    * If "Replace video with Diamond Movies" is On then "Manage Views" will be offered and take you to the "Custom Views Setup"

> |[http://dl.dropbox.com/u/7826058/Diamond/MoviesDiamonViewsJumpToOption-ppc.JPG](http://dl.dropbox.com/u/7826058/Diamond/MoviesDiamonViewsJumpToOption.JPG) _Jump To_|[http://dl.dropbox.com/u/7826058/Diamond/MoviesDiamonViewsJumpToList-ppc.JPG](http://dl.dropbox.com/u/7826058/Diamond/MoviesDiamonViewsJumpToList.JPG) _Diamond Views_| _click to view_ |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|
    * Info Options - this option will display a full or part screen info view when you "pause" on a movie for the specified delay - see options
      * Auto Fade - toggles this feature - on - off
      * Delay in MS to fade - 5000 (5 seconds) by default
      * Fade Style
        * !Fullscreen - Info Panel will consume the entire screen
        * Slide Panel - Info Panel will slide out from the right side (partial screen)
      * FullScreen Fanart - on for Fullscreen background or off for background in a window
      * SplitScreen - on - off
      * Info Shows Diamond Fade - if on the Info button will display the Diamond Info screen - on - off
|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeBackground-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeBackground.jpg) _FS Fanart On_ |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeBackgroundOff-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeBackgroundOff.jpg) _FS Fanart Off_ |
|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeSplitScreen-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeSplitScreen.jpg) _Split Screen_ |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeSlideInfo-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MoviesInfoFadeSlideInfo.jpg) _Slide Info_ | _click to view_ |

  * **Note:** options are saved per diamond movie view (including the custom views you create)

## Main Menu Mods ##
### Widgets ###
> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/WidgetControlPanel-small.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/WidgetControlPanel.jpg) _click to view_
> > |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/Widgets1-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/Widgets1.jpg) _Sample 1_ |![![](http://dl.dropbox.com/u/7826058/Diamond/Widgets2-ppc.jpg)](http://dl.dropbox.com/u/7826058/Diamond/Widgets2.jpg) _Sample 2_ | _click to view_ |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------|:----------------|
    * go to Options from the Main Menu - Diamond Options - Main Menu Widget Control Panel
    * **TIP:** Make sure you set **Show Widgets** to On
    * from here you can...
      * left panel - Layouts
        * select from valid widget layouts
        * S = 14%, M = 28%, L = 42% and XL = 56% of the vertical screen
        * select a layout and the Widget type selection buttons will change to match the layout
      * middle panel - Widget Type Selection
        * each button will toggle between the valid widget types
        * the Main Menu Widget will be updated dynamically while you select
        * you can choose between....
          * Basic Weather
          * Extended Weather
          * Weather Forecast
          * Recent Recordings
          * Upcoming Recordings
          * Recording Space
        * **Note:** the widget contents will adjust depending on the size of the panel you place it in. For example an XL forecast panel will include 5 days of forecast where a M panel will only include 3 days.
      * right panel - Widget Options
        * Use Tab Style Widgets - on - off
        * Widget Width - to adjust use left and right arrows while the slider has focus
      * **Note:** if the SageTV setting "Display Video on Menus" is on then the Main Menu Widgets will not display

### Default Videos Menu ###

> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuVideosCustom-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuVideosCustom.jpg) _click to view_
    * customize the default Videos view
      * go to Options from the Main Menu - Diamond Options - make sure "Replace Video with Diamond Movies" is Off
      * select from the various options to Show or Hide - these will show above the standard items (titles, DVD etc)
        * Wall Flow
        * Cover Flow
        * List Flow
        * Category Flow
        * 360 Flow
### Custom Movie Menu ###
    * replace the default Videos menu with the Diamond "Movies" menu
      * go to Options from the Main Menu - Diamond Options - make sure "Replace Video with Diamond Movies" is On
      * select "Custom Views Setup" to create your own Diamond Flow Views. From here you can....
> > > |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViews-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViews.jpg) _Customize_|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViewsAdd-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViewsAdd.jpg) _Add New_| _click to view_ |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|
        * "Add New View" (set the type and name it)
        * edit or delete views you previously created
          * select a view from the numbered list and press Ok
          * select "Delete View" from the menu to delete the custom View
          * select "Change View Type" from the menu to change the Flow type for the view
        * change the order of the custom flow views
          * place focus on a View Name and press the RIGHT arrow for options to move the view
        * "Edit Default Views to Show"
> > > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViewsShowDefaultViews-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuCustomMovieViewsShowDefaultViews.jpg) _click to view_
          * you can keep access to the Sage Default STV Video views
            * Titles
            * DVD/BD
            * Folders
            * Category
            * Playlists
            * VideoConversions
            * Play DVD
### Hide Main Menu Items ###

> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuShowHideMenuItems-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/MainMenuShowHideMenuItems.jpg) _click to view_
    * go to Options from the Main Menu - Diamond Options - Hide Main Menu Items
      * TV Menu - on - off
      * Show Video/Movies - on - off
      * Show Music - on - off
      * Show Photos - on - off
      * Show Search - on - off
      * Show Online - on - off
      * Show Setup - on - off
      * Show Exit - on - off

## Fanart Options ##
  * Folder level Fanart
> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/DetailedSetupFolderFanart-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/DetailedSetupFolderFanart.jpg) _click to view_
    * if you use Folder Level Posters or Backgrounds set those options in Detailed Setup - Diamond Fanart
    * select the type of fanart you use
  * Phoenix Fanart
    * check Detailed Setup - Diamond Fanart to ensure the Phoenix Fanart Directory is set to something valid for your extender/client to reach
    * check that Phoenix Fanart Enabled is set to "On".
  * Cache
    * to delete the cache so it will rebuild
      * select Detailed Setup - Diamond Fanart and select Delete All Cached Fanart
      * answer Yes to the prompt
      * Recommended - answer Yes to the next prompt to automatically Cache ALL fanart. Selecting No will cache fanart during the use of the Diamond Views.
> > > ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/CacheFanart-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/CacheFanart.jpg) _click to view_
    * Cache Posters While Idle - on - off
      * Number of Posters to Cache - Diamond will fetch this number of posters times 2 (forward and backward)
  * Single TV/Recording/Movie Fanart delete

> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/FanartDeleteSingleItem-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/FanartDeleteSingleItem.jpg) _click to view_
    * in any Diamond view press the Delete key on any item and a menu option including Delete Fanart will be offered
    * new fanart will immediately be available and displayed
  * Background Fanart
    * this feature can be turned on/off on the various Diamond Movie/TV Views on the Diamond Options menus
  * Series Fanart options
    * see the [#TV/Recordings\_Views](#TV/Recordings_Views.md) section

## OSD Customizations ##
  * While the OSD is displayed, hit Options...
> > |![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDOptions-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDOptions.jpg) _OSD Options_|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDNormal-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDNormal.jpg) _Diamond Normal OSD_|![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDSlim-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/OSDSlim.jpg) _Diamond Slim OSD_| _click to view_ |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|
    * Top OSD visible - can set this to Top OSD Hidden
    * Bottom OSD Slim Mode - on - off
    * Bottom OSD Sports Ticker Mode - will lift the Bottom OSD to allow for sports info to be seen - on - off
    * Bottom OSD Volume Bar - on - off
    * Play toggle OSD - if on then pressing play will display/hide the OSD - on - off

## TV Guide ##
  * Background Fanart

> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/TVGuideFanart-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/TVGuideFanart.jpg) _click to view_
    * the option is available on the Diamond Options menu

## Troubleshooting ##
  * Reset Diamond Settings
> ![![](http://babgvant.com/downloads/pluckyhd/DiamondImages/ResetDiamond-ppc.jpg)](http://babgvant.com/downloads/pluckyhd/DiamondImages/ResetDiamond.jpg) _click to view_
    * go to the Options menu from the Main Menu - Diamond Options
    * select "Debug Menu"
    * select "Clear All Diamond Settings"
    * select Delete Client (or Server) Settings
    * **Note:** this will delete the current local Fanart Cache and all custom views and settings and CANNOT be undone
