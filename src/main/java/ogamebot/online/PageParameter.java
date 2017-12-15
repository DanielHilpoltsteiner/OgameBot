package ogamebot.online;

/**
 *
 */
enum PageParameter {
    OVERVIEW("overview"),
    SUPPLY("resources"),
    FACILITIES("station"),
    MERCHANT("traderOverview"),
    RESEARCH("research"),
    SHIPYARD("shipyard"),
    DEFENCE("defense"),
    FLEET("fleet1"),
    GALAXYS("galaxy"),
    OFFICIERCASINO("premium"),
    SHOP("shop"),
    ALLIANCE("alliance"),
    LOGOUT("logout"),
    HIGHSCORE("highscore");

    private final String pageParameter;


    PageParameter(String pageParameter) {
        this.pageParameter = pageParameter;
    }

    public String getPageParameter() {
        return pageParameter;
    }
}
