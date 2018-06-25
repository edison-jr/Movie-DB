package com.edison.android.tools.media;

import android.support.annotation.NonNull;

public interface MediaWriter {

    int NO_FLAGS = -1;

    void into(@NonNull Media media, int flags);

}
