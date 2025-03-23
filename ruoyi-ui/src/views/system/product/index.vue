<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入产品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="价格范围" prop="price">
        <el-input-number
          v-model="queryParams.minPrice"
          :precision="2"
          :step="0.1"
          :min="0"
          size="small"
          style="width: 120px"
          placeholder="最小价格"
        />
        <span class="el-range-separator">-</span>
        <el-input-number
          v-model="queryParams.maxPrice"
          :precision="2"
          :step="0.1"
          :min="0"
          size="small"
          style="width: 120px"
          placeholder="最大价格"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:product:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:product:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品编号" align="center" prop="id" />
      <el-table-column label="产品编码" align="center" prop="code" />
      <el-table-column label="产品名称" align="center" prop="name" />
      <el-table-column label="产品价格" align="center" prop="price">
        <template slot-scope="scope">
          {{ scope.row.price ? '¥' + scope.row.price : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="产品排序" align="center" prop="sort" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:product:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.categoryId !== 0">
            <el-form-item label="上级类别" prop="parentId">
              <treeselect v-model="form.categoryId" :options="categoryOptions" :normalizer="normalizer" placeholder="选择上级类别" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="产品名称" required>
          <el-input v-model="form.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="产品编码" required>
          <el-input v-model="form.code" placeholder="请输入产品编码"></el-input>
        </el-form-item>
        <el-form-item label="产品标题" required>
          <el-input v-model="form.title" placeholder="请输入产品标题"></el-input>
        </el-form-item>
        <el-form-item label="产品排序">
          <el-input-number v-model="form.sort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="产品价格" prop="price">
          <el-input-number
            v-model="form.price"
            :precision="2"
            :step="0.1"
            :min="0"
            controls-position="right"
            placeholder="请输入产品价格"
          />
        </el-form-item>
        <!-- 图片上传部分 -->
        <el-form-item label="产品图片" prop="images">
          <el-upload
            class="upload-demo"
            action="/dev-api/common/upload"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            accept="image/*"
            multiple>
            <i class="el-icon-plus"></i>
          </el-upload>
          <!-- 图片预览对话框 -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
        <el-form-item label="产品内容" prop="context">
          <quill-editor
            ref="myQuillEditor"
            v-model="form.context"
            :options="editorOptions"
            @ready="onEditorReady"
            @focus="onEditorFocus"
            @blur="onEditorBlur"
            @change="onEditorChange"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/system/product";
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { Message } from "element-ui";
import axios from "axios";
import {getToken} from "@/utils/auth";
import data from "@/views/system/dict/data";
import {listCategory} from "@/api/system/category";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { upload } from "@/api/common";  // 确保这行引入正确

export default {
  components: { quillEditor,Treeselect},
  name: "Product",
  dicts: ['sys_normal_disable'],
  data() {
    const that = this; // 保存 vue 实例引用
    return {
      // 上传相关数据
      fileList: [],
      dialogImageUrl: '',
      dialogVisible: false,
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      // 重新渲染表格状态
      refreshTable: true,
      // 是否展开，默认全部展开
      isExpandAll: true,
      categoryOptions: [],
      quillInstance : null,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 产品表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        name: undefined,
        minPrice: undefined,
        maxPrice: undefined
      },
      // 表单参数
      form: {
        id: undefined,
        code: undefined,
        name: undefined,
        title: undefined,
        sort: 0,
        status: "0",
        context: "",
        categoryId: undefined,
        images: "",
        price: undefined
      },
      editorOptions: {
        theme: 'snow',
        modules: {
          toolbar: {
            container: [
              ['bold', 'italic', 'underline', 'strike'],
              ['blockquote', 'code-block'],
              [{ 'header': 1 }, { 'header': 2 }],
              [{ 'list': 'ordered'}, { 'list': 'bullet' }],
              [{ 'script': 'sub'}, { 'script': 'super' }],
              [{ 'indent': '-1'}, { 'indent': '+1' }],
              [{ 'direction': 'rtl' }],
              [{ 'size': ['small', false, 'large', 'huge'] }],
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'color': [] }, { 'background': [] }],
              [{ 'font': [] }],
              [{ 'align': [] }],
              ['clean'],
              ['link', 'image', 'video']
            ],
            handlers: {
              image: function() {
                const input = document.createElement('input');
                input.setAttribute('type', 'file');
                input.setAttribute('accept', 'image/*');
                input.click();
                
                input.onchange = () => {
                  const file = input.files[0];
                  const formData = new FormData();
                  formData.append('file', file);
                  
                  // 使用保存的 vue 实例引用
                  that.uploadImage(formData, this.quill);
                };
              }
            }
          }
        },
        placeholder: '请输入产品内容...'
      },
      // 表单校验
      rules: {
        categoryId: [
          { required: true, message: "上级类别不能为空", trigger: "blur" },
        ],
        images: [
          { required: true, message: '请上传产品图片', trigger: 'change' }
        ],
        name: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "产品编码不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "产品价格不能为空", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value < 0) {
                callback(new Error('价格不能小于0'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCategoryTree();
  },
  mounted() {
    // 设置编辑器默认字体大小
    if (this.$refs.myQuillEditor) {
      const quill = this.$refs.myQuillEditor.quill;
      quill.format('size', 'normal');
    }
  },
  methods: {
    // 上传前的验证
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
        return false;
      }
      return true;
    },
    // 上传成功的回调
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        // 将新的图片URL添加到已有的图片字符串中
        let imageUrls = this.form.images ? this.form.images.split(',') : [];
        imageUrls.push(response.url);
        this.form.images = imageUrls.join(',');

        // 更新文件列表显示
        this.fileList = fileList;
        this.$message.success('图片上传成功');
      } else {
        this.$message.error(response.msg || '上传失败');
        // 从文件列表中移除上传失败的文件
        const index = fileList.indexOf(file);
        if (index > -1) {
          fileList.splice(index, 1);
        }
      }
    },

    // 移除图片
    handleRemove(file, fileList) {
      // 从图片字符串中移除对应的URL
      let imageUrls = this.form.images.split(',');
      const index = imageUrls.indexOf(file.url || file.response.url);
      if (index > -1) {
        imageUrls.splice(index, 1);
        this.form.images = imageUrls.join(',');
      }
      // 更新文件列表
      this.fileList = fileList;
    },

    // 预览图片
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    getCategoryTree() {
      this.loading = true;
      listCategory(undefined).then(response => {
        this.categoryOptions = this.handleTree(response.data, "id");
        this.loading = false;
      });
    },
    /** 转换类别数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      };
    },
    /** 查询产品列表 */
    getList() {
      this.loading = true;
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        name: undefined,
        title: undefined,
        sort: 0,
        status: "0",
        context: "",
        categoryId: undefined,
        images: "",
        price: undefined
      };
      this.fileList = [];
      this.dialogImageUrl = '';
      this.dialogVisible = false;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getCategoryTree();

      this.open = true;
      this.title = "添加产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getProduct(id).then(response => {
        this.form = response.data;
        // 如果有富文本内容，进行解码
        if (this.form.context) {
          try {
            this.form.context = decodeURIComponent(atob(this.form.context));
          } catch (error) {
            console.error("内容解码失败", error);
            this.$modal.msgError("内容解码失败");
          }
        }
        // 处理图片列表
        if (this.form.images) {
          const imageUrls = this.form.images.split(',');
          this.fileList = imageUrls.map(url => ({
            name: url.substring(url.lastIndexOf('/') + 1),
            url: url
          }));
        }
        this.open = true;
        this.title = "修改产品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const submitData = { ...this.form };
          // 提交前编码富文本内容
          if (submitData.context) {
            try {
              submitData.context = btoa(encodeURIComponent(submitData.context));
            } catch (error) {
              console.error("内容编码失败", error);
              this.$modal.msgError("内容编码失败");
              return;
            }
          }

          if (submitData.id != undefined) {
            updateProduct(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(submitData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除产品编号为"' + ids + '"的数据项？').then(function() {
        return delProduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
    },
    // 编辑器就绪
    onEditorReady(editor) {
      this.quill = editor;
    },
    // 编辑器获得焦点
    onEditorFocus(editor) {
      console.log('编辑器获得焦点', editor);
    },
    // 编辑器失去焦点
    onEditorBlur(editor) {
      console.log('编辑器失去焦点', editor);
    },
    // 编辑器内容改变
    onEditorChange({ editor, html, text }) {
      console.log('编辑器内容改变:', html);
    },
    // 新增上传图片方法
    uploadImage(formData, quill) {
      const loading = this.$loading({
        lock: true,
        text: '图片上传中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      upload(formData).then(response => {
        loading.close();
        if (response.code === 200) {
          const range = quill.getSelection(true);
          quill.insertEmbed(range.index, 'image', response.url);
          quill.setSelection(range.index + 1);
          this.$modal.msgSuccess("图片上传成功");
        } else {
          this.$modal.msgError(response.msg || "图片上传失败");
        }
      }).catch(error => {
        loading.close();
        console.error('上传失败:', error);
        this.$modal.msgError("图片上传失败");
      });
    },
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.upload-demo {
  width: 100%;
}

.upload-demo .el-upload {
  margin-right: 10px;
}

.upload-demo .el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}

.el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

.el-upload-list--picture-card {
  display: inline;
  margin: 0;
}

.dialog-content {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
}

.product-dialog {
  display: flex;
  flex-direction: column;
}

.product-dialog .el-dialog__body {
  flex: 1;
  overflow: auto;
}

.el-range-separator {
  padding: 0 5px;
}

.el-input-number.is-controls-right .el-input__inner {
  padding-left: 5px;
  padding-right: 30px;
}

.el-input-number {
  width: 180px;
}
</style>
