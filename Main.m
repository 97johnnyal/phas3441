clear()
%Postion parameters - cheat to start simulation
x_pos = 0.01;
y_pos = 0.002;
v_x = 0;
v_y = 0;

% System parameters
drop_radius = 0.4*10^-3
oil_density = 0.971
mass_droplet = 2.6*10^-7;
force_freq = 80;
T_F = 2/force_freq;
k = (4*mass_droplet*(pi^2))/((0.250)^2);
g= -9.81
% Viscous Damping
D = 0;
% Wave Force
F = 1.3174e-6;
K_F = 1250;
Amplitude = F/(mass_droplet*K_F*-9.81)
% Memory
Me = 150;

%Simulation parameters
stored_wave = 4;
grid_step = 0.001;
grid_size = 50*grid_step;
total_grid_points = (2*grid_size)/grid_step + 1
grid_center= grid_size/grid_step +1 
t_step=T_F;


%Animation stuff
fig_1 = figure;
p = uipanel('Parent',fig_1); 
p.Title = 'A'; 
p.TitlePosition = 'centertop'; 
p.FontSize = 12;
p.FontWeight = 'bold';
frame = 0


n = sym('n')

point_array = nan(stored_wave,3);%Preallocates array that contains list of impact point/times
%[point_array(1,1),point_array(1,2)] = deal(x_pos,y_pos);
[point_array(1,1),point_array(1,2),point_array(1,3)] = deal(x_pos,y_pos,0);
wave = zeros(total_grid_points,total_grid_points,stored_wave); %Preallocates wave array


for t_current = 0:t_step:5
    frame = frame+1
    
    parfor (point = 1:stored_wave,4) %Generates each point's field
        %coordintes and time for generation
        x_gen_pos = point_array(point,1)
        y_gen_pos = point_array(point,2)
        t = t_current-point_array(point,3)
        t = t_current
        %Math
        if not(isnan(x_gen_pos)|isnan(y_gen_pos)|isnan(point_array(point,3)))
            new_wave_term = comp_wave_field_term( x_gen_pos,y_gen_pos,t,Amplitude,K_F,T_F,Me,grid_step,grid_size);
            wave(:,:,point) = new_wave_term
        end
    end
    wf= sum(wave,3) %Sums all waves
    
    %calculates change in velocity
    [grad_x,grad_y] = gradient(wf);
    delta_v_x = mass_droplet*g*grad_x(grid_center+int16((x_pos/grid_step)));
    delta_v_y = mass_droplet*g*grad_y(grid_center+int16((y_pos/grid_step)));
    v_x = v_x + delta_v_x;
    v_y = v_y + delta_v_y;
    
    %Updates postion
    delta_x = v_x*t_step ;
    delta_y = v_y*t_step;
    x_pos = x_pos + delta_x;
    y_pos = y_pos + delta_y;
    next_postion = [x_pos,y_pos,t_current]
    
    %Replaces oldest wave in memory with new one
    point_to_overwrite = rem(frame,stored_wave)+1;
    [point_array(point_to_overwrite,1),point_array(point_to_overwrite,2),point_array(point_to_overwrite,3)]= deal(next_postion(1),next_postion(2),next_postion(3));
    
    %postion_log = postion_log    %Keeps array of all postions for bug testing/plotting
    
    
    %adds frame to animation
    subplot(1,1,1,'parent',p)
    contourf(wf)
    caxis([-1.2*10^-3 ,1.2*10^-3])
    colormap('cool')
    colorbar
    M(frame) = getframe;
end
