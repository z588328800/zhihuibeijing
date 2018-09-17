package priv.zl.zhihuibeijing.domain;

import java.util.ArrayList;

/**
 * 分类信息封装
 * <p>
 * 使用Gson解析时,对象书写技巧: 1. 逢{}创建对象, 逢[]创建集合(ArrayList) 2. 所有字段名称要和json返回字段高度一致
 *
 * @author Kevin
 * @date 2015-10-18
 */
public class NewsMenu {

    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;

    public NewsMenu() {
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public void setExtend(ArrayList<Integer> extend) {
        this.extend = extend;
    }

    public void setData(ArrayList<NewsMenuData> data) {
        this.data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public ArrayList<Integer> getExtend() {
        return extend;
    }

    public ArrayList<NewsMenuData> getData() {
        return data;
    }

    // 侧边栏菜单对象
    public class NewsMenuData {
        public int id;
        public String title;
        public int type;

        public ArrayList<NewsTabData> children;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setChildren(ArrayList<NewsTabData> children) {
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getType() {
            return type;
        }

        public ArrayList<NewsTabData> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return "NewsMenuData [title=" + title + ", children=" + children
                    + "]";
        }
    }

    // 页签的对象
    public class NewsTabData {
        public int id;
        public String title;
        public int type;
        public String url;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "NewsTabData [title=" + title + "]";
        }

    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }
}
