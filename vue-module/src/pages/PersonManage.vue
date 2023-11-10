<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface Person {
  username: string
  password?: string
  roles?: string
}

interface Page {
  size: number
  totalElements: number
  number: number
}

const persons = ref<Person[]>([])
const page = reactive<Page>({
  size: 10,
  totalElements: 0,
  number: 1
})
const personForm = reactive<Person>({
  username: '',
  password: '123456',
  roles: 'user'
})
const personFormRef = ref<FormInstance>()
const rules = reactive<FormRules<Person>>({
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
    { min: 4, max: 16, message: 'Length should be 4 to 16', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
    { min: 4, max: 16, message: 'Length should be 4 to 16', trigger: 'blur' }
  ]
})

const fetchPersons = async () => {
  const responsePromise = await fetch(`/api/persons?page=${page.number - 1}&size=${page.size}`)
  const response = await responsePromise.json()
  persons.value = response._embedded.persons
  page.totalElements = response.page.totalElements
}

const deleteUser = async (username: string) => {
  if (username === 'admin') {
    return
  }
  await fetch('/api/persons/' + username, {
    method: 'DELETE',
  })
  page.number = 1
  fetchPersons()
}

const addUser = async (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return
  }
  await formEl.validate(async valid => {
    if (!valid) {
      return
    }
    await fetch('/api/persons/' + personForm.username, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(personForm)
    })
    fetchPersons()
  })
}

fetchPersons()

</script>

<template>
  <el-form ref="personFormRef" :model="personForm" :rules="rules" label-width="120px" class="demo-ruleForm" status-icon>
    <el-form-item label="Username" prop="username">
      <el-input v-model="personForm.username" />
    </el-form-item>
    <el-form-item label="Password" prop="password">
      <el-input v-model="personForm.password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="addUser(personFormRef)">
        Add User
      </el-button>
    </el-form-item>
  </el-form>
  <el-table :data="persons" style="width: 100%">
    <el-table-column prop="username" label="Username" width="180" />
    <el-table-column fixed="right" label="Operations" width="120">
      <template #default="scope">
        <el-button link type="primary" size="small" @click.prevent="deleteUser(scope.row.username)">
          Delete
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination layout="prev, pager, next" :total="page.totalElements" v-model:current-page="page.number"
    @update:current-page="fetchPersons" />
</template>

<style scoped></style>
