import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Topbar from './Topbar';
import Sidebar from './Sidebar';
import AdminDashboard from '../../pages/admin/AdminDashboard';
import TeacherDashboard from '../../pages/teacher/TeacherDashboard';
import StudentDashboard from '../../pages/student/StudentDashboard';
import UserCreateForm from '../../components/forms/UserCreateForm';
import UserList from '../../components/lists/UserList';
import NotificationCreateForm from '../../components/forms/NotificationCreateForm';
import StudentHistoryCreateForm from '../../components/forms/StudentHistoryCreateForm';
import StudentHistoryList from '../../components/lists/StudentHistoryList';
import ProfilePage from '../../pages/ProfilePage';
import NotificationList from '../../components/lists/NotificationList';
import TrainingGroupForm from '../forms/TrainingGroupFrom';
import TrainingGroupList from '../lists/TrainingGroupList';
import TrainingGroupStudentManager from '../forms/TrainingGroupsStudentManager';
import AssistanceForm from '../forms/AssitanceForm';
import AssistanceList from '../lists/AssistanceList';
import TrainingSessionList from '../lists/TrainingSessionList';
import ViewTimetable from '../forms/ViewTimetable';
import MembershipDetails from '../forms/MembershipDetails';
import MembershipList from '../lists/MembershipList';
import InvoiceForm from '../forms/InvoiceForm';
import InvoiceList from '../lists/InvoiceList';
import PaymentForm from '../forms/PaymentForm';
import ProductForm from '../forms/ProductForm';
import ProductList from '../lists/ProductList';
import IVATypeManager from '../forms/IVATypeManager';
import '../styles/MainLayout.css';

const MainLayout = () => {
  return (
    <div className="main-layout">
      <Topbar />
      <div className="main-content">
        <Sidebar />

        <div className="content-area">
          <Routes>
            {/* Dashboards */}
            <Route path="/admin/dashboard" element={<AdminDashboard />} />
            <Route path="/teacher/dashboard" element={<TeacherDashboard />} />
            <Route path="/student/dashboard" element={<StudentDashboard />} />

            {/* User Management */}
            <Route path="/admin/user-management/users/create" element={<UserCreateForm />} />
            <Route path="/admin/user-management/users/list" element={<UserList />} />
            <Route path="/admin/user-management/notifications/create" element={<NotificationCreateForm />} />
            <Route path="/admin/user-management/notifications/list" element={<NotificationList />} />
            <Route path="/admin/user-management/student-history/create" element={<StudentHistoryCreateForm />} />
            <Route path="/admin/user-management/student-history/list" element={<StudentHistoryList />} />

            {/* Class Management */}
            <Route path="/admin/class-management/training-groups/create" element={<TrainingGroupForm />} />
            <Route path="/admin/class-management/training-groups/list" element={<TrainingGroupList />} />
            <Route path="/admin/class-management/training-groups/manage-students" element={<TrainingGroupStudentManager />} />
            <Route path="/admin/class-management/assistance/create" element={<AssistanceForm />} />
            <Route path="/admin/class-management/assistance/list" element={<AssistanceList />} />
            <Route path="/admin/class-management/training-session/list" element={<TrainingSessionList />} />
            <Route path="/admin/class-management/training-groups/view-timetable" element={<ViewTimetable />} />
            <Route path="/admin/class-management/memberships/details" element={<MembershipDetails />} />
            <Route path="/admin/class-management/memberships/list" element={<MembershipList />} />

            {/* Finance */}
            <Route path="/admin/finance/invoices/create" element={<InvoiceForm />} />
            <Route path="/admin/finance/invoices/list" element={<InvoiceList />} />
            <Route path="/admin/finance/payments/create" element={<PaymentForm />} />
            <Route path="/admin/finance/products/create" element={<ProductForm />} />
            <Route path="/admin/finance/products/list" element={<ProductList />} />
            <Route path="/admin/finance/ivatypes/create" element={<IVATypeManager />} />

            {/* Perfil */}
            <Route path="/profile" element={<ProfilePage />} />
          </Routes>
        </div>
      </div>
    </div>
  );
};

export default MainLayout;
