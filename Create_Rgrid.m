function [ R_grid ] = Create_Rgrid( grid_step,grid_size,point_source_coordinate )
%Creates a 2D array of the absolute distance (R value) from a point
%   Takes the following inputs:
%
%   grid_step(Scalar) - Resolution of simulation in arbitray units
%   
%   grid_size(2x1 vector) - determines total volume to be simulated. 
%   Input should be a the maxium of the simulation in each dimension in
%   x,y order and in the same units as grid step. A value of n simulates
%   from -n to n along that dimension
%   
%   point_source_coordinate(2x1 vector) - The Orign of the pulse given as 3 vector
%   where each element is the corresponding x,y coordinate of the pulse in units of grid step 
%   
%   Returns the following outputs:
%
%   R_grid(3D array of size 'grid_size') - The output is an array with
%   every element giving the absolute distance ,R, from
%   point_source_coordinate
%
%   Notes: 
%   Function produces a grid which is of equal length in all dimensions and
%   the origin (0,0,0) is in the centre of the grid
%   Function Takes pulse_source_coordinate as distance from a central
%   origin
%   grid_size is the the maximum axis value. That is to say a grid size of n 
%   arbitray units takes the range -n to n



% Sets origin values and source coordinates

[x_s,y_s] = deal(point_source_coordinate(1),point_source_coordinate(2));

% Creates 2 1D vectors
xVar = [-grid_size:grid_step:grid_size];
yVar = [-grid_size:grid_step:grid_size];


%Meshgrid of axis - Nesscary to create array of coordinate values
[X,Y] = meshgrid(xVar,yVar);

%R_grid is calculated 
R_grid = sqrt((abs(X-x_s)).^2 + (abs(Y-y_s)).^2 );

end

