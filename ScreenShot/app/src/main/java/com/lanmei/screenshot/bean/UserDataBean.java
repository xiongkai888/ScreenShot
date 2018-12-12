package com.lanmei.screenshot.bean;

import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

/**
 * Created by xkai on 2018/6/7.
 */

public class UserDataBean<T> extends DataBean<T> {

    public UserBean getData(){
        try {
            UserBean bean = JsonUtil.jsonToBean(Des.decode((String) data), UserBean.class);
            L.d("BeanRequest", "用户信息getData：" + Des.decode((String) data));
            L.d("BeanRequest", "订单详情：" + Des.decode("xVkrs2Yjt8LndVny3MvnlcJ4bQEpisXwuDqo8uuqrJmyJ+qarmiuZB8G9\\/1ExqDbhWrbMJLkj5VnN\\/ADcfj6O9oDHyljqUGXFAjiIdkPcG20arCe6BfvXTro9KylGgvnk1VLHpwTfedpZ4itOfrVDaWL3PqI2cprQ74DE064H4xXwMsROxwb7RI\\/Ptm5U75Z3rVi+ZeZBmWvLwgAZ9Q8YHweSlLU\\/q0Df21YKMkHCbim\\/9DukYpU1pKQ5EsfFkKrr1CiQK1+dUfqhhiqfttmVio46nxTAEJcdzaz7BUC+k+jtK92q8fYCS7rBYU9PNCcw2mfvPSXCuUw2fy607\\/ptR96gVLsrdUUvFScHBYlIJT6rG6Ln02CZoEm8QqfBCFT66bELkrmrixXHyqNJbP3W1WdbRUzoKxdXopvB4qbrDM3mb1Q2PTVjiqE9GM8wr4ILcV50omvPucCt9280hKf00aECIEpsLLM\\/rqVQZgU+L4JiEeFpL\\/gkxNfamoUBI551n\\/LgX3mHBDDi1lkA6Qm\\/d7YqEk0Tp3H50ec3T2i19fJbaeLZtFOSRNQjsOD7JRR\\/KgTfsiIpGG6bPTEtUrCwAfjTp0W43MXsuDG4WzIAalYrC6g\\/91lZnKYdrwz105E70Mku8LALBVUKtBdOWV9m+1a2uL9S\\/5HM06ODD0XOg6Q+AG15Cwt1THUD4jagKCBfSJIOvrML8IU8SxSXkbtXa8xyB4nicX\\/oNWLVfAIcKrydBQnNEoABAeppE6TK6yGcXVrRejxelRdR4YFk98SgYRT0uG9cONpqYfNYCt2j80EUiPRYO8K4iQHV7zWVq8BlaOhAOm1jPWOQrk9knA0dZUp3AFxeAx9o3mJuE82S8NH78eRi9KhhYUkMrC8HwagqXTZbitfXzZaXoliwivZeTfAYMbxIrg93ITxGCl+Oni0J64keRSa0\\/9ZucoHguqqyxP7dLWaHz96y3xKAvoS0CaRJQpAA\\/wpsiQeMCMMxOmuLm3vXMG\\/EXu+L6e2XPL7L931fC4YEUNeT1hj7vwvg8b+8HWDgo55Zo7bJ\\/LW\\/LpI88ITr5I3CE1NFqNxDkXDbntdbVsoIY0jPigP0Duguts72y6BZhNT6j3cIIUygzgM\\/NX4Wf0VFkcIykjBpEHGyT+Nbs\\/oHlm49iLTRR34aWtapI\\/0BAmZtsZKUH\\/n3Ix8fCrsrSKw56TIUwZwX3ZokJVptpCDbG7ZYO3dv+lHHpCrFOlxYp77XiNkPvXGNGg5djx\\/lkRWw\\/gP2KWL6B0nYbgVlYE+cdaNjWF7syrSNAjORPRp1NRTax2mUYb8j9kT+htTuTmgflUSD5GUTwVNL0kY7jnw6L\\/50bIUEp3cO+PawxC15qAk9CGsJDw9fVxL2pabHKG213mujk6hmVBqMmk0ejyYbo1Klpqqi2NUz\\/SLcJrmgK30B7ug8oOi4Ht4HZpiTlvvAGtqGG6hW8DA\\/rtDm50aNCrBWWgaTAAd\\/L0SdG+uYSsPIDxi3qSmXdpqQEY\\/9Thn9H+chdjj+ZHoRzqlhD\\/93l0+eVv11wq\\/j+Gy1EdI15\\/+HPJXLsJXVHFp9mlr34Wip6JMrBe78f\\/WkxggLxReTZgPQeiLwGGkYwZDKD938fN\\/MzwK0REW7fXmanHRd0kNm8L2Yv0N9VdSWKX8B3Zc69m3Hot\\/yKbF2YzzpV6HFwG8jU9QJhn1W4oIF9xmal+gCWTUxukNOGBtSaXLt0Af5j14sEDOXJLSYCe04y9+sVoOM9YB+0Y+3xHjx+roS4gusfCd1fKW5a0NdNhmzQBiZJI\\/\\/8aICDMIcsfc5zRZWFU98DjFZ3kGKXQJg+YATg1xe07pamaQNKEbTtIFx\\/lrw9VoCUcb0FAtF6ErtysgEMRdZ1IdNfoVnvXSeTOB6IgdkKs2BtzLMIiViNWs7xDJD5iqHasCkyVTdOKcDENynXYN2KfrDewn+I4T9deF5gmiOsODnc1s9nVOgpwXb9YTNUlArbZslVm4kCpqdUgvW6t+oLOyS2RHhYJQeyvkOlXLM8mh00JZN7MbICTc6yy0RgG5hVMHJtn\\/asuO86A7SD+OR\\/IJMUy0e\\/hSlky00MzdNuOMq59FAJMScHKzlmbrhizqDyRSyDJEKZ1Zoh3WLt7JKeytMide+ALwyoL3krD\\/WcQ191m70aE5e91HNz7jWE5a\\/itAscI+UfG2Mqji\\/dPcdfrJvBPSd9pc7h2bYUd1pRfV1OV2dYSkq8y1DXk2FiXBOU0Yux715COXi7J1PVeVEglFvQyQOA92O3AlHsXYomErTw7ZJZF6DRTkKZL3Ruk\\/9SahRHgXYv25udoIsTUlFtB+\\/8RRU3r9KgFeOXKtM9XZ7VjeAMMflM5cz6iD\\/u1I2k8UcUqFDQrpf0gxu\\/KcwYMzIOjbcjcRahuXV2YmXJ4Xuajc3VQl1FQJdyOQ8snp\\/zz\\/nUuVPzXrgJ242o2FM0xFKzQTD3UAdqOtS4b7\\/YIRLl0FMXe7sAIbtRfVIo4+eRi0SuY\\/5+Ym2+86gHb2mT9P5c7SKoR59pDoVI7KpdoI\\/iGrMvRAyjHhu\\/Xxxyucte3JWyIzKqthjKD8sQDKh5IJy0\\/9Y6qVLDqOQfqp7iBriDsxW8DN1\\/hr6P5RhSyk\\/T6yiAOIcsDAlkfhBR8GWmOWpMRH\\/cnTC8+ykbz8Img2HJf\\/CBgXn7LbrXth8sp7FmedP3gSypEc1+kJCWg2aJTh9eVDqx7Sg+yLqkpKcaey2ymWw5r+FMwvjceHynBSc3IY9DDH3x5AJcbravhl6d7v\\/beb230l65wBeKg3zuNf8ZWvEWCQSrpup3bGs0VbW0Yvd1SGh1QSODaCatg3QUOvOKh2ygpQ2XZK7iqKLD\\/171iaUZOXVx07Cl3sjhc5KjGjDpllZ9TrCjBB1L0uxcFyoPi8L1VMB0WqY3icR63ri\\/F7v4vdI++mY4nPXrnTxb9tbrb5kxtvhUzconZZvXIU8+5eZc7J49ptUTmSQcbeU5abauvqD1mCxkbdNiclfi0wyOASofRI7Gqczijnqb+vbCecdGV1n5SKaBm5hR+F6JEm1IJsAJ7NUYrTt5K3p7X5NgOebNDsrlOSuT2KPwUdCDXlk2vr\\/UagCBIUAKkOkjQI\\/hngw0xM84L+FuK5w55\\/IBGz2sI1OxfC9nlmPdZlkrN6I27nJgkgRXgfJsqUOOf737mFoLVZ\\/v7bgcIyb05dJOd2rlsqzTjvFqA9"));
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
