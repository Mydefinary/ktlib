##
<template>
    <v-container>
        <v-snackbar
            v-model="snackbar.status"
            :timeout="snackbar.timeout"
            :color="snackbar.color"
        >
            
            <v-btn style="margin-left: 80px;" text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
        <div class="panel">
            <div class="gs-bundle-of-buttons" style="max-height:10vh;">
                <v-btn @click="addNewRow" @class="contrast-primary-text" small color="primary">
                    <v-icon small style="margin-left: -5px;">mdi-plus</v-icon>작가 등록 신청
                </v-btn>
                <!-- <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog()" class="contrast-primary-text" small color="primary">
                    <v-icon small>mdi-pencil</v-icon>포트폴리오 URL 수정
                </v-btn> -->
            </div>
            <AuthorList @search="search" style="margin-bottom: 10px; background-color: #ffffff;"></AuthorList>
            <div class="mb-5 text-lg font-bold"></div>
            <div class="table-responsive">
                <v-table>
                    <thead>
                        <tr>
                        <th>순서</th>
                        <th>이름</th>
                        <th>닉네임</th>
                        <th>연락처</th>
                        <th>이메일</th>
                        <th>포트폴리오Url</th>
                        <th>승인여부</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(val, idx) in value" 
                            @click="changeSelectedRow(val)"
                            :key="val"  
                            :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;':''"
                        >
                            <td class="font-semibold">{{ idx + 1 }}</td>
                            <td class="whitespace-nowrap" label="AuthorName">{{ val.authorName }}</td>
                            <td class="whitespace-nowrap" label="AuthorNickname">{{ val.authorNickname }}</td>
                            <td class="whitespace-nowrap" label="PhoneNumber">{{ val.phoneNumber }}</td>
                            <td class="whitespace-nowrap" label="Email">{{ val.email }}</td>
                            <td class="whitespace-nowrap" label="PortfolioUrl">{{ val.portfolioUrl }}</td>
                            <td class="whitespace-nowrap" label="Status">{{ val.status }}</td>
                            <v-row class="ma-0 pa-4 align-center">
                                <v-spacer></v-spacer>
                                <Icon style="cursor: pointer;" icon="mi:delete" @click="deleteRow(val)" />
                            </v-row>
                        </tr>
                    </tbody>
                </v-table>
            </div>
        </div>
        <v-col>
            <v-dialog
                v-model="openDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">작가 등록</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <Author :offline="offline"
                            :isNew="!value.idx"
                            :editMode="true"
                            :inList="false"
                            v-model="newValue"
                            @add="append"
                        />
                    </v-card-text>
                </v-card>
            </v-dialog>
            <!-- <v-dialog
                v-model="editDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Author 수정</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <div>
                            <div>
                                <String
                                    style="width: 100%;"
                                    label="PortfolioUrl"
                                    v-model="selectedRow.portfolioUrl"
                                    :editMode="true"
                                />
                            </div>
                            <v-divider class="my-4" />
                            <v-row justify="end">
                                <v-btn width="64px" color="primary" @click="save">수정</v-btn>
                            </v-row>
                        </div>
                    </v-card-text>
                </v-card>
            </v-dialog> -->
        </v-col>
    </v-container>
</template>

<script>
import { ref } from 'vue';
import { useTheme } from 'vuetify';
import BaseGrid from '../base-ui/BaseGrid.vue'


export default {
    name: 'authorGrid',
    mixins:[BaseGrid],
    components:{
    },
    data: () => ({
        path: 'authors',
    }),
    watch: {
    },
    methods:{
    }
}

</script>