package com.busap.vcs.data.mapper;


import com.busap.vcs.data.model.ComboBoxData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/9/22.
 */
@Repository("comboBoxDao")
public interface ComboBoxDao {

    List<ComboBoxData> selectMusicTypeComboBox();

    List<ComboBoxData> selectUserComboBox();

    List<ComboBoxData> selectComboBoxLabels();

    List<ComboBoxData> selectActivityCombobox();

    List<ComboBoxData> selectPermissionCombobox();

    List<ComboBoxData> selectUserComboBoxByType(Map<String,Object> map);

    List<ComboBoxData> selectOrganizationComboBox();

    List<ComboBoxData> selectSingerTypeComboBox();

    List<ComboBoxData> selectSingerComboBox();

    List<ComboBoxData> selectAlbumComboBox();

    List<ComboBoxData> selectRegPlatformCombobox();

}
