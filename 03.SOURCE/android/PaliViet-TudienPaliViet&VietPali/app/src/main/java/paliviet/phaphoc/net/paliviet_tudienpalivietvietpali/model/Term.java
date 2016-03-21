package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.model;

/**
 * Created by hkhoi on 3/21/16.
 */
public class Term {

    private String key;

    private String definition;

    private String source;

    private boolean favorite;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
