<template>
  <div>
    <el-card>
      <el-row :gutter="24">
        <el-col :span="8">
          <el-input v-model="queryInfo.query" placeholder="请输入内容">
            <template #append>
              <el-button @click="getUserList">
                搜索
              </el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="addDialogVisible = true">添加讲师</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="exportInstructors">导出讲师</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
        </el-col>
        <el-col :span="10">
          <el-upload
              :limit="1"
              :show-file-list="false"
              :auto-upload="false"
              :on-change="upload"
              accept=".xlsx"
              ref="uploadFile"
          >
            <el-button type="success">上传文件</el-button>
          </el-upload>
        </el-col>
      </el-row>
      <!-- 用户列表区域  -->
      <el-table :data="instructorList" border class="table-with-margin" stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="讲师姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="contactInfo"></el-table-column>
        <el-table-column label="邮箱" prop="email"></el-table-column>
        <el-table-column label="职称" prop="title"></el-table-column>
        <el-table-column label="擅长的领域" prop="expertiseArea" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="300px">
          <template v-slot="scope">
            <!-- 修改按钮 -->
            <el-button size="mini" type="primary" @click="openChangeCourse(scope.row)">修改</el-button>
            <!-- 删除按钮 -->
            <el-button size="mini" type="danger" @click="deleteInstructor(scope.row)">删除</el-button>
            <!-- 查看所授课程按钮 -->
            <el-button size="mini" type="success" @click="searchCourse(scope.row)">查看所授课程</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加用户的对话框 -->
      <el-dialog v-model="addDialogVisible" title="添加讲师" width="40%">
        <!-- 内容主体区域 -->
        <el-form ref="addFormRef" :model="addForm" label-width="auto">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="addForm.name"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="contactInfo">
            <el-input v-model="addForm.contactInfo"></el-input>
          </el-form-item>
          <el-form-item label="Email" prop="email">
            <el-input v-model="addForm.email"></el-input>
          </el-form-item>
          <el-form-item label="职称" prop="title">
            <el-input v-model="addForm.title"></el-input>
          </el-form-item>
          <el-form-item label="擅长的领域" prop="expertiseArea">
            <el-input v-model="addForm.expertiseArea"></el-input>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addInstructor">确定</el-button>
      </span>
        </template>
      </el-dialog>
      <!-- 修改用户的对话框 -->
      <el-dialog v-model="changeDialogVisible" title="修改讲师" width="40%">
        <!-- 内容主体区域 -->
        <el-form ref="addFormRef" :model="changeForm" label-width="auto">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="changeForm.name"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="contactInfo">
            <el-input v-model="changeForm.contactInfo"></el-input>
          </el-form-item>
          <el-form-item label="Email" prop="email">
            <el-input v-model="changeForm.email"></el-input>
          </el-form-item>
          <el-form-item label="职称" prop="title">
            <el-input v-model="changeForm.title"></el-input>
          </el-form-item>
          <el-form-item label="擅长的领域" prop="expertiseArea">
            <el-input v-model="changeForm.expertiseArea"></el-input>
          </el-form-item>
        </el-form>
        <!-- 底部区域 -->
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="changeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changeInstructor">确定</el-button>
      </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import {context} from "ant-design-vue/es/vc-image/src/PreviewGroup";
import {ElMessageBox, ElMessage} from "element-plus";

export default {
  data() {
    return {
      // 获取用户列表的参数对象
      queryInfo: {
        query: '', // 查询参数
      },
      // 用户列表
      instructorList: [],
      addDialogVisible: false,
      changeDialogVisible: false,
      approvalDialogVisible: false,
      // 添加用户的表单数据对象
      addForm: {
        name: '',
        contactInfo: '',
        email: '',
        title: '',
        expertiseArea: '',
        employeeType: '2'
      },
      changeForm: {
        employeeId: '',
        name: '',
        contactInfo: '',
        email: '',
        title: '',
        expertiseArea: ''
      },
    }
  },
  created() {
    //接收参数
    const name = this.$route.query.name;
    //console.log(name);
    this.getUserList()
  },
  methods: {
    getUserList() {
      axios
          .get("/executor/Instructor/getAllTeachers")
          .then(
              response => {
                this.instructorList = response.data;
              },
              response => {
                console.log("error");
                alert("请求失败");
              }
          );
    },
    exportInstructors() {
      axios({
        url: `/executor/Instructor/export`,
        method: 'GET',
        responseType: 'arraybuffer', // 设置响应数据类型为 arraybuffer
      })
          .then(response => {
            const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }); // 创建一个 Blob 对象
            const url = window.URL.createObjectURL(blob); // 创建一个 URL 对象
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', '讲师信息.xlsx'); // 设置下载文件名，确保是 Excel 格式（.xlsx）
            document.body.appendChild(link);
            link.click(); // 模拟点击链接进行下载
            link.parentNode.removeChild(link); // 下载完成后移除元素
          })
          .catch(error => {
            // 处理下载失败的情况
            // console.error('下载失败', error);
            ElMessage.error('下载失败');
          });
    },
    downloadTemplate() {
      axios({
        url: `/executor/Instructor/importTemplate`,
        method: 'GET',
        responseType: 'arraybuffer', // 设置响应数据类型为 arraybuffer
      })
          .then(response => {
            const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }); // 创建一个 Blob 对象
            const url = window.URL.createObjectURL(blob); // 创建一个 URL 对象
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', '讲师导入模板.xlsx'); // 设置下载文件名，确保是 Excel 格式（.xlsx）
            document.body.appendChild(link);
            link.click(); // 模拟点击链接进行下载
            link.parentNode.removeChild(link); // 下载完成后移除元素
          })
          .catch(error => {
            // 处理下载失败的情况
            // console.error('下载失败', error);
            ElMessage.error('下载失败');
          });
    },
    upload(file) {
      const formData = new FormData();
      formData.append('instructors', file.raw);

      axios.post('/executor/Instructor/import', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
          .then(response => {
            if (response.data.flag) {
              ElMessage.success('上传成功');
            } else {
              ElMessage.error(response.data.errorMsg);
            }
          })
          .catch(() => {
            ElMessage.error('上传失败');
          });
      this.$refs.uploadFile.clearFiles()
    },
    //删除讲师
    deleteInstructor(row) {
      // 弹出确认对话框，确认后执行删除操作
      //console.log("row"+row.courseId);
      ElMessageBox.confirm('确定删除[' + row.name + ']的信息吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 发送删除请求
        axios.get(`/executor/Instructor/delete?id=${row.employeeId}`)
            .then(response => {
              this.$message.success('删除成功');
              // 删除成功后刷新数据
              this.getUserList();
            })
            .catch(error => {
              console.error("删除失败", error);
              this.$message.error('删除失败');
            });
      }).catch(() => {
        // 用户取消删除操作
      });
    },
    //打开修改框的时候把原来的数据填上
    openChangeCourse(row) {
      this.changeForm.employeeId = row.employeeId;
      this.changeForm.name = row.name;
      this.changeForm.contactInfo = row.contactInfo;
      this.changeForm.email = row.email;
      this.changeForm.title = row.title;
      this.changeForm.expertiseArea = row.expertiseArea;
      this.changeDialogVisible = true;
    },
    //修改讲师
    changeInstructor() {
      console.log("changeForm" + this.changeForm.name)
      axios
          .post("/executor/Instructor/update", this.changeForm)
          .then((response) => {
            this.$message.success("修改成功");
            // 添加成功后刷新数据
            this.getUserList();
            // 关闭添加对话框
            this.changeDialogVisible = false;
          })
          .catch((error) => {
            console.error("修改失败", error);
            this.$message.error("修改失败");
          });
      // 关闭编辑对话框
      this.changeDialogVisible = false;
    },
    //新建讲师,关闭对话框
    addInstructor() {
      axios
          .post("/executor/Instructor/add", this.addForm)
          .then((response) => {
            this.$message.success("添加成功");
            // 添加成功后刷新数据
            this.addForm.name = '';
            this.addForm.contactInfo = '';
            this.addForm.email = '';
            this.addForm.title = '';
            this.addForm.expertiseArea = '';
            this.getUserList();
            // 关闭添加对话框
            this.addDialogVisible = false;
          })
          .catch((error) => {
            console.error("添加失败", error);
            this.$message.error("添加失败");
          });
      // 关闭编辑对话框
      this.addDialogVisible = false;
    },
    //跳转并传参
    searchCourse(row){
       //console.log("row.employeeId"+row.employeeId);
      //this.$router.push({name:'ExecutorInstructorCourse',query:{name:row.employeeId}});
      // axios
      //     .get(`/instructor/my/information?employee_id=${row.employeeId}`)
      //     .then(
      //         response => {
      //           const name = response.data.data.name;
      //           // this.$emit('ListenChild', { page: 2,name:"name"});
      //         },
      //         response => {
      //           console.log("error");
      //           alert("请求失败");
      //         }
      //     );
      this.$emit('ListenChild', { page: 2,name:"name"});
      },
  }

}
</script>

<style scoped>
.table-with-margin {
  margin-top: 20px; /* 调整表格与上方内容的间距，根据实际需要设置 */
}
</style>