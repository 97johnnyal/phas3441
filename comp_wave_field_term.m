
function [ wave_field_term ] = comp_wave_field_term( x_pos,y_pos,t,Amplitude,K_F,T_F,Me,grid_step,grid_size)
%UNTITLED3 Summary of this function goes here
%   Creates for a set point and set time since generation. Currently very
%   ineffciently called at each time point, rather than generating a time
%   based array for a point or something similar
R = Create_Rgrid(grid_step,grid_size,[x_pos,y_pos]);
n = sym('n');
f = Amplitude*besselj(1,K_F.*R)*exp(-(t-n*T_F)/(T_F*Me));
wave_field_term = double(symsum(f,n,0,int16((t/T_F))));

end
