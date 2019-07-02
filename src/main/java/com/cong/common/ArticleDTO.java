package com.cong.common;

import java.util.List;
import lombok.Data;

/**
 * @Description TODO
 * @Author zheng cong
 * @Date 2019-06-28
 */
@Data
public class ArticleDTO {

    private String carModel;

    private String promotionDateBegain;

    private String promotionDateEnd;

    private List<CarInfoDTO> carInfoDTOS;

    private String title;

    private String shortTitle;

    private String introduction;

    private GiftPackDTO giftPackDTO;

    private List<String> overheadInformations;

}
