<template>
<v-container>
<!-- Snackbar -->
<v-snackbar v-model="snackbar.status" :timeout="snackbar.timeout" :color="snackbar.color">
<v-btn style="margin-left: 80px;" text @click="snackbar.status = false">Close</v-btn>
</v-snackbar>
 
    <!-- 버튼 영역 -->
<div class="panel">
<div class="gs-bundle-of-buttons" style="max-height:10vh;">
<v-btn @click="addNewRow" class="contrast-primary-text" small color="primary">
<v-icon small style="margin-left: -5px;">mdi-plus</v-icon>등록
</v-btn>
<v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog" class="contrast-primary-text" small color="primary">
<v-icon small>mdi-pencil</v-icon>수정
</v-btn>
<v-btn :disabled="!selectedRow" style="margin-left: 5px;" color="green" small @click="publishbook">
          출간요청
</v-btn>
</div>
 
      <!-- 목록 -->
<ManuscriptList @search="search" style="margin-bottom: 10px; background-color: #ffffff;" />
 
      <!-- 테이블 -->
<div class="table-responsive">
<v-table>
<thead>
<tr>
<th>Id</th>
<th>Title</th>
<th>Content</th>
<th>Status</th>
<th>Author</th>
<th>Updated At</th>
</tr>
</thead>
<tbody>
<tr
              v-for="(val, idx) in value"
              :key="val"
              :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;' : ''"
              @click="changeSelectedRow(val)"
>
<td>{{ idx + 1 }}</td>
<td>{{ val.title }}</td>
<td style="max-width: 250px;" class="text-truncate">
                {{ val.content.length > 100 ? val.content.slice(0, 100) + '...' : val.content }}
</td>
<td>{{ val.status || '-' }}</td>
<td>{{ val.authorNickname }} (#{{ val.authorId }})</td>
<td>{{ val.lastModified || val.createdDate }}</td>
<v-row class="ma-0 pa-4 align-center">
<v-spacer></v-spacer>
<Icon style="cursor: pointer;" icon="mi:delete" @click.stop="deleteRow(val)" />
</v-row>
</tr>
</tbody>
</v-table>
</div>
</div>
 
    <!-- 등록 다이얼로그 -->
<v-dialog v-model="openDialog" transition="dialog-bottom-transition" width="40%">
<v-card>
<v-toolbar color="primary" class="elevation-0 pa-4" height="50px">
<div style="color:white; font-size:17px; font-weight:700;">Manuscript 등록</div>
<v-spacer></v-spacer>
<v-icon color="white" small @click="closeDialog()">mdi-close</v-icon>
</v-toolbar>
<v-card-text>
<Manuscript
            :value="newValue"
            :editMode="true"
            :isNew="true"
            @update:value="v => newValue = v"
            @add="append"
          />
</v-card-text>
</v-card>
</v-dialog>
 
    <!-- 수정 다이얼로그 -->
<v-dialog v-model="editDialog" transition="dialog-bottom-transition" width="40%">
<v-card>
<v-toolbar color="primary" class="elevation-0 pa-4" height="50px">
<div style="color:white; font-size:17px; font-weight:700;">Manuscript 수정</div>
<v-spacer></v-spacer>
<v-icon color="white" small @click="closeDialog()">mdi-close</v-icon>
</v-toolbar>
<v-card-text class="scroll-wrapper">
<Manuscript
            :value="editingRow"
            :editMode="true"
            :isNew="false"
            @update:value="v => editingRow = v"
            @save="saveEdit"
          />
</v-card-text>
</v-card>
</v-dialog>
</v-container>
</template>
 
<script>
import axios from 'axios';
import BaseGrid from '../base-ui/BaseGrid.vue';
import ManuscriptRepository from '../repository/ManuscriptRepository';
import Manuscript from '../Manuscript.vue';
 
export default {
  name: 'manuscriptGrid',
  mixins: [BaseGrid],
  components: { Manuscript },
  data: () => ({
    path: 'manuscripts',
    openDialog: false,
    editDialog: false,
    newValue: {},
    editingRow: {}
  }),
  created() {
    this.repository = new ManuscriptRepository(axios);
    this.search();
  },
  methods: {
    async publishbook() {
      if (!this.selectedRow) return;
      try {
        if (this.selectedRow.status === 'RequestedPublish') {
          throw '이미 처리된 요청입니다';
        }
        const id = this.selectedRow._links.self.href.split('/').pop();
        await this.repository.publishBook(id);
        this.success('출간 요청 되었습니다.');
        await this.search();
      } catch (e) {
        this.error(e);
      }
    },
 
    async saveEdit(updatedData) {
      try {
        updatedData.lastModified = new Date().toISOString();
        updatedData.status = 'MODIFIED';
        await this.repository.save(updatedData, false);
        this.success('수정 완료');
        this.editDialog = false;
        await this.search();
      } catch (e) {
        this.error(e);
      }
    },
 
    addNewRow() {
      this.newValue = {
        title: '',
        content: '',
        authorId: null,
        authorNickname: '',
        createdDate: new Date().toISOString(),
        status: 'DRAFT'
      };
      this.openDialog = true;
    },
 
    openEditDialog() {
      this.editingRow = { ...this.selectedRow };
      this.editDialog = true;
    }
  }
};
</script>
 
<style scoped>
.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.scroll-wrapper {
  max-height: 75vh;
  overflow-y: auto;
  padding: 12px;
}
</style>