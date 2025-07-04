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
        <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog()" class="contrast-primary-text" small color="primary">
          <v-icon small>mdi-pencil</v-icon>수정
        </v-btn>
        <v-btn :disabled="!selectedRow" style="margin-left: 5px;" color="green" small @click="publishbook">
          출간요청
        </v-btn>
      </div>

      <ManuscriptList @search="search" style="margin-bottom: 10px; background-color: #ffffff;"></ManuscriptList>
      <div class="mb-5 text-lg font-bold"></div>

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
              @click="changeSelectedRow(val)"
              :key="val"
              :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;' : ''"
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
            v-model="newValue"
            :editMode="true"
            :isNew="true"
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
          <String label="Title" v-model="selectedRow.title" :editMode="true" class="mb-3" />
          <v-textarea label="Content" v-model="selectedRow.content" rows="10" outlined dense class="mb-3" />
          <Date label="Created Date" v-model="selectedRow.createdDate" :editMode="false" class="mb-3" />
          <Date label="Last Modified" v-model="selectedRow.lastModified" :editMode="false" class="mb-3" />

          <v-divider class="my-4"></v-divider>

          <v-layout row justify-end>
            <v-btn width="64px" color="primary" @click="saveEdit">수정</v-btn>
          </v-layout>
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

    async saveEdit() {
      try {
        this.selectedRow.lastModified = new Date().toISOString();
        this.selectedRow.status = 'MODIFIED';
        await this.repository.save(this.selectedRow, false);
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
  },
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
