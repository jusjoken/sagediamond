/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import sagex.phoenix.factory.BaseConfigurable;
import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.vfs.IMediaFile;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.groups.IGrouper;
import sagex.phoenix.vfs.groups.TitleGrouper;
import sagex.phoenix.vfs.util.HasOptions;

/**
 *
 * @author jusjoken
 */
public class FirstLetterTitleRegexGrouper extends TitleGrouper implements HasOptions {
	/**
	 * {@value}
	 */
	
	private List<ConfigurableOption> options = new ArrayList<ConfigurableOption>();
	private Pattern pattern = null;

	private IGrouper grouper; 
	
    public FirstLetterTitleRegexGrouper(IGrouper grouper) {
		this.grouper = grouper;  
    }
	@Override
	public List<ConfigurableOption> getOptions() {
		return options;
	}

	@Override
	public void onUpdate(BaseConfigurable parent) {
		String pat = "^(?:(?:the|a|an)\\s+)?(\\S)";
		if (pat!=null) {
			pattern = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
		} else {
			pattern = null;
		}
	}

	@Override
	public String getGroupName(IMediaResource res) {
		String grp = grouper.getGroupName(res);
		if (pattern !=null && res instanceof IMediaFile) {
			if (grp!=null) {
				Matcher m = pattern.matcher(grp);
                                if (m.find()) {
                                    if (m.groupCount()>0){
                                        grp = m.group(1);
                                    }else{
                                        grp = grp.substring(m.start(), m.end());
                                    }
                                }
			}
		}
		return grp;
	}    
}
