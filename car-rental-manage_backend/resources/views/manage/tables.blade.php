@extends('layouts.app')

@section('content')  
        <!-- Breadcrumbs-->
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <a href="{{route('home')}}">Dashboard</a>
          </li>
          <li class="breadcrumb-item active">Tables</li>
        </ol>
             
        <!-- DataTables Example -->
        <div class="card mb-3">
          <div class="card-header">
            <i class="fas fa-table"></i>
            User's Profile</div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Birth Day</th>
                    <th>Phone</th>
                    <th>Address</th>
                  </tr>
                </thead>
                <tfoot>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Birth Day</th>
                    <th>Phone</th>
                    <th>Address</th>
                  </tr>
                </tfoot>
                <tbody>
                  @foreach($tables as $table)
                  <tr>
                    <td> {{$table->id}} </td>
                    <td> {{$table->name}} </td>
                    <td> {{$table->gender}} </td>
                    <td> {{$table->birth_day}} </td>
                    <td> {{$table->phone}} </td>
                    <td> {{$table->address}} </td>
                  </tr>
                  @endforeach
                </tbody>
              </table>
            </div>
          </div>
          <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
        </div>

        <p class="small text-center text-muted my-5">
          <em>More table examples coming soon...</em>
        </p>
@endsection
