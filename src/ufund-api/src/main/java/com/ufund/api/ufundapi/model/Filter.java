package com.ufund.api.ufundapi.model;

import java.io.IOException;

public interface Filter {

    public Need[] applyFilter(boolean topDown, FilterType filter) throws IOException;

}
