<template>
  <el-form :model="form" ref="form" label-width="100px">
    <el-form-item label="文章内容">
      <div ref="editor" class="editor"></div>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import Quill from 'quill';

export default {
  data() {
    return {
      form: {
        content: '',  // 用于存储编辑器内容
      },
      editor: null,  // Quill 编辑器实例
    };
  },
  mounted() {
    // 初始化 Quill 编辑器
    this.editor = new Quill(this.$refs.editor, {
      theme: 'snow',
      modules: {
        toolbar: [
          [{ 'header': '1' }, { 'header': '2' }],
          [{ 'list': 'ordered'}, { 'list': 'bullet' }],
          ['bold', 'italic', 'underline'],
          ['link'],
        ],
      },
    });
  },
  methods: {
    submitForm() {
      // 获取编辑器的内容并赋值到 form.content
      this.form.content = this.editor.root.innerHTML;
      console.log('表单内容:', this.form);

      // 你可以发送 AJAX 请求来提交表单
      // axios.post('/your/api/endpoint', this.form).then(response => {
      //   console.log(response.data);
      // });
    },
  },
};
</script>

<style>
.editor2 {
  height: 300px;
}
</style>
