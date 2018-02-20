package com.tseluikoartem.ening.yandexmobdevproject;




import junit.framework.Assert;

import org.junit.Test;

import static org.mockito.Mockito.*;
import java.util.ArrayList;

import launcher.AppModel;
import launcher.IconRecycleAdapter;


public class LauncherAdapterTest {


    @Test
    public void testAdapterData() {
        ArrayList<AppModel> data= mock(ArrayList.class);
        int layouttype=1;

        IconRecycleAdapter adapter = mock(IconRecycleAdapter.class);
        Assert.assertNotNull(adapter.getData());

    }

}