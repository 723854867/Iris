package com.busap.vcs.service;

import com.busap.vcs.data.model.ComboBoxData;

import java.util.List;

/**
 * Created by huoshanwei on 2015/9/22.
 */
public interface ComboBoxService {

    List<ComboBoxData> queryMusicTypeComboBox(String selected);

    List<ComboBoxData> queryUserComboBox(String selected);

    List<ComboBoxData> queryComboBoxLabels(String selected);

    List<ComboBoxData> queryActivityCombobox(String selected);

    List<ComboBoxData> queryPermissionCombobox(String selected);

    List<ComboBoxData> queryUserComboBoxByType(String selected,Integer type);

    List<ComboBoxData> queryOrganizationList(String selected);

    List<ComboBoxData> selectSingerTypeComboBox(String selected);

    List<ComboBoxData> selectSingerComboBox(String selected);

    List<ComboBoxData> selectAlbumComboBox(String selected);

    List<ComboBoxData> selectRegPlatformCombobox(String selected);
}
